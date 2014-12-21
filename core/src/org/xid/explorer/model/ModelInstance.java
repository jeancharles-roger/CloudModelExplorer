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

import org.xid.explorer.dsl.DslInstance;

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
}