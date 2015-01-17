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
import org.xid.explorer.result.ModelResultHandler.CompositeModelResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Utility methods for ModelResults.
 */
public class ModelResultUtil {

    /**
     * Loads ModelResultHandler from a List of ModelResultDescription.
     *
     * @param context the exploration context.
     * @param resultDescriptions the descriptions to load.
     * @return a ModelResultHandler that contains all described ones (in a CompositeModelResultHandler if needed).
     * @throws Exception if a ModelResultHandler can't be instantiated for any reason.
     */
    public static ModelResultHandler loadModelExplorationHandler(ExplorationContext context, List<ModelResultDescription> resultDescriptions) throws Exception {
        // handles null and empty
        if (resultDescriptions == null || resultDescriptions.isEmpty()) return ModelResultHandler.EMPTY;

        // collects all model result factories declared in classpath.
        Map<String, ModelResultFactory> factories = new HashMap<>();
        for (ModelResultFactory factory : ServiceLoader.load(ModelResultFactory.class)) {
            for (String type : factory.getKnownTypes()) {
                factories.put(type, factory);
            }
        }

        // instantiates all model result handlers.
        ModelResultHandler[] results = new ModelResultHandler[resultDescriptions != null ? resultDescriptions.size() : 0];
        if (resultDescriptions != null) {
            for (int i = 0; i < results.length; i++) {
                ModelResultDescription resultDescription = resultDescriptions.get(i);
                ModelResultFactory factory = factories.get(resultDescription.getType());
                if (factory == null) throw new Exception("No factory found for type '" + resultDescription.getType() + "'");
                results[i] = factory.createResult(context, resultDescription);
            }
        }

        return results.length == 1 ? results[0] : new CompositeModelResultHandler(results);
    }
}
