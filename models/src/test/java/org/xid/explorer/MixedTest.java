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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MixedTest {

    @Test
    public void test1() throws Exception {
        // creates description by API for test.
        ModelDescription description = new ModelDescription();
        List<DslInstanceDescription> instances = new ArrayList<>();
        description.setName("MixedTest.test1");

        DslInstanceDescription lambdaInstance = new DslInstanceDescription();
        lambdaInstance.setDsl("explorer.lambda");
        lambdaInstance.setName("one");
        lambdaInstance.setSize(4);
        lambdaInstance.getDslParameters().put("class", "org.xid.explorer.LambdaTest");
        lambdaInstance.getDslParameters().put("field", "TRANSITION_TEST1_1");
        instances.add(lambdaInstance);

        description.setInstances(instances);

        DslInstanceDescription luaInstance = new DslInstanceDescription();
        luaInstance.setDsl("explorer.lua");
        luaInstance.setName("two");
        luaInstance.setSize(4);
        luaInstance.getResources().add("test1.lua");
        instances.add(luaInstance);

        ResourceResolver resourceResolver = new PathResourceResolver(Paths.get("lua/test1/"));
        TestUtil.explore(description, resourceResolver, null, 121, 231);
    }

    @Test
    public void test2() throws Exception {
        TestUtil.explore("mixed/test2/", 13, 13);
    }
}
