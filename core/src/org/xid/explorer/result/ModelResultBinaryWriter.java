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

import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;

import java.io.OutputStream;

/**
 * ModelExplorationBinaryWriter generate a binary format from the exploration result.
 */
public class ModelResultBinaryWriter implements ModelResultHandler {

    private final ModelInstance modelInstance;

    private final OutputStream out;

    public ModelResultBinaryWriter(ModelInstance modelInstance, OutputStream out) {
        this.modelInstance = modelInstance;
        this.out = out;
    }

    @Override
    public void begin() {
    }

    @Override
    public void state(ModelState state) {

    }

    @Override
    public void transition(ModelState source, ModelTransition transition, ModelState target) {
    }

    @Override
    public void end() {
    }

}
