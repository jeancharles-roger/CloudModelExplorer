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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Wrapper to serialize data exchanges in Hazelcast using Kryo
 */
public class KryoSerializer<T> implements StreamSerializer<T> {

    private static int seed = 1;

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return new Kryo();
        }
    };

    private final int id;
    private final Class<T> type;

    public KryoSerializer(Class<T> type) {
        this.id = seed += 1;
        this.type = type;
        kryoThreadLocal.get().register(type);
    }

    public int getTypeId() {
        return id;
    }

    public void write(ObjectDataOutput objectDataOutput, T object) throws IOException {
        Kryo kryo = kryoThreadLocal.get();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        Output output = new Output(deflaterOutputStream);
        kryo.writeObject(output, object);
        output.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        objectDataOutput.write(bytes);
    }

    public T read(ObjectDataInput objectDataInput) throws IOException {
        Input input = new Input(new InflaterInputStream((InputStream) objectDataInput));
        Kryo kryo = kryoThreadLocal.get();
        return kryo.readObject(input, type);
    }

    public void destroy() {
    }
}
