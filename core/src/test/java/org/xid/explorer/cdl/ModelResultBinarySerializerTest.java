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

package org.xid.explorer.cdl;

import org.junit.Test;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;
import org.xid.explorer.result.ModelResultBinaryReader;
import org.xid.explorer.result.ModelResultHandler;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by j5r on 15/01/2015.
 */
public class ModelResultBinarySerializerTest {

     private void readKryoFile(String path, int expectedStates, int expectedTransitions) throws IOException {
         ModelResultBinaryReader reader = new ModelResultBinaryReader(new ModelResultHandler() {

            int states = 0;
            int transitions = 0;

            @Override
            public void begin() throws IOException {
            }

            @Override
            public void state(ModelState state) {
                states+=1;
                //System.out.println("-------------------");
                //System.out.println(state);
            }

            @Override
            public void transition(ModelTransition transition) {
                transitions+=1;
                //System.out.println(transition);
            }

            @Override
            public void end() throws IOException {
                assertEquals(expectedStates, states);
                assertEquals(expectedTransitions, transitions);
            }
         });
         long start = System.currentTimeMillis();
         reader.read(new FileInputStream(path));
         long end = System.currentTimeMillis();System.out.println();

         System.out.println("Read '" + path + "' in " + (end-start) + " ms.");
    }

    @Test
    public void readFileCdl1() throws IOException {
        readKryoFile("kryo/cdl1.kryo", 2, 1);
    }

    @Test
    public void readFileCdl2() throws IOException {
        readKryoFile("kryo/cdl2.kryo", 16, 32);
    }

    @Test
    public void readFileLua1() throws IOException {
        readKryoFile("kryo/lua1.kryo", 1331, 3630);
    }
}
