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

package org.xid.explorer.message;

import org.xid.explorer.model.ModelDescription;

/**
 * An ExploreMessage contains an exploration order for a slave
 */
public class ExploreMessage {

    private final ModelDescription modelDescription;

    public ExploreMessage(ModelDescription modelDescription) {
        this.modelDescription = modelDescription;
    }

    public ModelDescription getModelDescription() {
        return modelDescription;
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "]" + modelDescription;
    }
}
