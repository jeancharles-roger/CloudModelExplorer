/*
 * Copyright 2015 to CloudModelExplorer authors
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

package org.xid.explorer.result;

import org.xid.explorer.ExplorationContext;
import org.xid.explorer.result.ModelExplorationHandler.ModelExplorationVisitorComposite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by j5r on 14/01/2015.
 */
public class ModelResultUtil {

    public static ModelExplorationHandler loadModelExplorationHandler(ExplorationContext context, List<ModelResultDescription> resultDescriptions) throws Exception {
        // collects all model result factories declared in classpath.
        Map<String, ModelResultFactory> factories = new HashMap<>();
        for (ModelResultFactory factory : ServiceLoader.load(ModelResultFactory.class)) {
            for (String type : factory.getKnownTypes()) {
                factories.put(type, factory);
            }
        }

        ModelExplorationHandler[] results = new ModelExplorationHandler[resultDescriptions != null ? resultDescriptions.size() : 0];
        if (resultDescriptions != null) {
            for (int i = 0; i < results.length; i++) {
                ModelResultDescription resultDescription = resultDescriptions.get(i);
                ModelResultFactory factory = factories.get(resultDescription.getType());
                if (factory == null)
                    throw new Exception("No factory found for type '" + resultDescription.getType() + "'");
                results[i] = factory.createResult(context, resultDescription);
            }
        }
        return new ModelExplorationVisitorComposite(results);
    }
}
