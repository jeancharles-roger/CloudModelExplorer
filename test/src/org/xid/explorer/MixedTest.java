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

package org.xid.explorer;

import org.junit.Test;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.model.ModelInstance;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MixedTest {

    @Test
    public void test1() throws Exception {
        // creates description by API for test.
        ModelDescription description = new ModelDescription();
        description.setName("MixedTest.test1");

        DslInstanceDescription lambdaInstance = new DslInstanceDescription();
        lambdaInstance.setDsl("explorer.lambda");
        lambdaInstance.setName("one");
        lambdaInstance.setSize(2);
        lambdaInstance.getParameters().put("class", "org.xid.explorer.LambdaTest");
        lambdaInstance.getParameters().put("field", "TRANSITION_TEST1_1");
        description.getInstances().add(lambdaInstance);


        DslInstanceDescription luaInstance = new DslInstanceDescription();
        luaInstance.setDsl("explorer.lua");
        luaInstance.setName("two");
        luaInstance.setSize(2);
        luaInstance.getResources().add("test1.lua");
        description.getInstances().add(luaInstance);

        ResourceResolver resourceResolver = path -> Files.newInputStream(Paths.get("resource/lua/test1/" + path));
        TestUtil.explore(ModelInstance.load(description, resourceResolver), 121, 231);
    }

    @Test
    public void test2() throws Exception {
        TestUtil.explore("resource/mixed/test2/", 13, 13);
    }
}
