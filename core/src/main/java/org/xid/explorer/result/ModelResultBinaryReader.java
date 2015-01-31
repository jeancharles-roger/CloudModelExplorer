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
import com.esotericsoftware.kryo.io.Input;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

/**
 * ModelExplorationBinaryWriter reads a binary format of exploration result.
 */
public class ModelResultBinaryReader {

    private final ModelResultHandler handler;

    public ModelResultBinaryReader(ModelResultHandler handler) {
        this.handler = handler;
    }

    public void read(InputStream inputStream) throws IOException {
        Input input = new Input(new InflaterInputStream(inputStream));
        Kryo kryo = new Kryo();

        handler.begin();
        kryo.readObjectOrNull(input, Object.class);
        while (input.eof() == false) {
            handler.state(kryo.readObject(input, ModelState.class));

            ModelTransition transition = kryo.readObjectOrNull(input, ModelTransition.class);
            while (transition != null) {
                handler.transition(transition);
                transition = kryo.readObjectOrNull(input, ModelTransition.class);
            }
        }
        handler.end();
    }
}
