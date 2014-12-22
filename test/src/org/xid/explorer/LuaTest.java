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

import org.junit.Rule;
import org.junit.Test;
import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.model.ModelInstance;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LuaTest {

    @Rule
    public ModelName modelName = new ModelName();

    @Test
    public void test1() throws Exception {
        ResourceResolver resourceResolver = path -> Files.newInputStream(Paths.get("resource/lua/test1/" + path));

        // TODO transition count is different from same model in Lambda, to check.
        ModelDescription description = ModelDescription.loadDescription(resourceResolver.resolve("model.json"));
        TestUtil.explore(ModelInstance.load(description, resourceResolver), 1331, 3630);
    }

    @Test
    public void test2() throws Exception {
        ResourceResolver resourceResolver = path -> Files.newInputStream(Paths.get("resource/lua/test2/" + path));

        // TODO states and transition count are different from same model in Lambda, to check.
        ModelDescription description = ModelDescription.loadDescription(resourceResolver.resolve("model.json"));
        TestUtil.explore(ModelInstance.load(description, resourceResolver), 14, 15);
    }

}
