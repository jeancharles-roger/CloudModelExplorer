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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * ModelExplorationBinaryWriter generate a binary format from the exploration result.
 */
public class ModelResultBinaryWriter implements ModelResultHandler {

    private final ModelInstance modelInstance;

    private final Output out;

    private final Kryo kryo;

    public ModelResultBinaryWriter(ModelInstance modelInstance, OutputStream out) {
        this.modelInstance = modelInstance;
        this.out = new Output(new DeflaterOutputStream(out));
        this.kryo = new Kryo();
    }

    @Override
    public void begin() {
    }

    @Override
    public void state(ModelState state) {
        kryo.writeObjectOrNull(out, null, Object.class);
        kryo.writeObject(out, state);
    }

    @Override
    public void transition(ModelTransition transition) {
        kryo.writeObject(out, transition);
    }

    @Override
    public void end() throws IOException {
        kryo.writeObjectOrNull(out, null, Object.class);
        out.close();
    }

}
