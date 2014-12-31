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

package org.xid.explorer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Assert;
import org.junit.Test;
import org.xid.explorer.dsl.BinaryDslState;
import org.xid.explorer.model.ModelDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by j5r on 29/12/2014.
 */
public class ComplexStateTest {

    @Test
    public void test1() throws IOException {


        ModelDescription description = ModelDescription.loadDescription(new PathResourceResolver(Paths.get("resource/lua/test1/")).readEntry("model.json"));

        long start = System.currentTimeMillis();
        int n = 100_00;
        for (int i=0; i< n; i+=1) {
            BinaryDslState state = new BinaryDslState(100);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            Output output = new Output(new DeflaterOutputStream(byteArrayStream));
            Kryo kryo = new Kryo();
            kryo.writeObject(output, description);
            output.close();
            state.setBytes(byteArrayStream.toByteArray());
            Assert.assertEquals(141, state.getBytes().length);
        }
        long end = System.currentTimeMillis();

        System.out.println("Serialized deflated model " + n + " times in " + (end-start) + " ms.");

        start = System.currentTimeMillis();
        for (int i=0; i< n; i+=1) {
            BinaryDslState state = new BinaryDslState(100);
            Output output = new Output(0,-1);
            Kryo kryo = new Kryo();
            kryo.writeObject(output, description);
            output.close();
            state.setBytes(output.toBytes());
            Assert.assertEquals(222, state.getBytes().length);
        }
        end = System.currentTimeMillis();

        System.out.println("Serialized flat model " + n + " times in " + (end-start) + " ms.");
    }
}
