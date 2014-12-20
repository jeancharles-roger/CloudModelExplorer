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
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.lua.LuaInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LuaTest {

    @Test
    public void test1() throws IOException {
        Globals lua = JsePlatform.standardGlobals();

        String script = new String(Files.readAllBytes(Paths.get("resource/test1.lua")));
        DslInstance instance = new LuaInstance(2, script, lua);
        DslInstance[] instances = new DslInstance[] {
                instance, instance, instance
        };

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }
}
