/*
 * Copyright 2015 to CloudModelExplorer
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

package org.xid.explorer.model;

import org.xid.explorer.ResourceResolver;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.dsl.DslRuntime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * A ModelInstance represents an instantiated model to explore. A model instance is composed of a set of DslInstance.
 */
public final class ModelInstance {

    private final ModelDescription description;

    private final DslInstance[] instances;

    public ModelInstance(ModelDescription description, DslInstance[] instances) {
        this.description = description;
        this.instances = instances;
    }

    public ModelDescription getDescription() {
        return description;
    }

    public DslInstance[] getInstances() {
        return instances;
    }

    /**
     * Loads a ModelInstance from a ModelDescription.
     *
     * @param description description to load.
     * @param resourceResolver function that take a String and returns an open InputStream (or null).
     * @return a ModelInstance
     * @throws Exception if the description isn't found or some resources aren't present.
     */
    public static ModelInstance load(ModelDescription description, ResourceResolver resourceResolver) throws Exception {

        // collects all runtimes declared in classpath.
        Map<String, DslRuntime> runtimes = new HashMap<>();
        for (DslRuntime runtime : ServiceLoader.load(DslRuntime.class)) {
            runtimes.put(runtime.getId(), runtime);
        }

        List<DslInstanceDescription> instanceDescriptions = description.getInstances();
        if (instanceDescriptions == null || instanceDescriptions.isEmpty()) {
            throw new Exception("No instance declared for model.");
        }

        DslInstance[] dslInstances = new DslInstance[instanceDescriptions.size()];
        // creates a DslInstance for each DslInstanceDescription
        for (int i = 0; i < instanceDescriptions.size(); i++) {
            DslInstanceDescription instanceDescription = instanceDescriptions.get(i);

            DslRuntime runtime = runtimes.get(instanceDescription.getDsl());
            if (runtime == null) throw new Exception("No runtime '"+ instanceDescription.getDsl() +"' found");

            dslInstances[i] = runtime.createInstance(instanceDescription, resourceResolver);

        }
        return new ModelInstance(description, dslInstances);
    }

}
