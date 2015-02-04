package org.xid.explorer;/*
 * Copyright 2015 to org.xid.explorer.CloudModelExplorer authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.result.ModelExploration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "explore", description = "Explore a model")
public class ExploreCommand extends ExplorerCommand
{
    @Arguments(description = "Models to explore")
    public List<String> models;

    @Override
    public void run() {
        if (models.isEmpty()) {
            // no model to explore
            info("No models to explore.");
        } else {
            for (String model : models) {
                Path modelPath = Paths.get(model);
                if (Files.exists(modelPath)) {
                    ResourceResolver resourceResolver = new PathResourceResolver(modelPath);
                    try {
                        info("Loading model '"+ model +"'");
                        ModelDescription description = ModelDescription.loadDescription(resourceResolver.readEntry("model.json"));
                        BFSExplorer explorer = new BFSExplorer(description, resourceResolver);
                        explorer.initialize(null);
                        ModelExploration modelExploration = explorer.explore();
                        info(modelExploration.toString());
                    } catch (Exception e) {
                        error("Couldn't explorer model '" + model +"'", e);
                    }

                } else {
                    error("Model '"+ model +"' doesn't exist");
                }
            }
        }


    }

}
