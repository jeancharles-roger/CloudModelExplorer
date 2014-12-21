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
import org.luaj.vm2.luajc.LuaJC;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.lua.LuaInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LuaTest {

    @Test
    public void test1() throws IOException {
        Globals lua = JsePlatform.standardGlobals();
        LuaJC.install(lua);

        String script = new String(Files.readAllBytes(Paths.get("resource/test1/test1.lua")));
        DslInstance instance = new LuaInstance(2, script, lua);
        DslInstance[] instances = new DslInstance[] {
                instance, instance, instance
        };

        // TODO transition count is different from same model in Lambda, to check.
        TestUtil.explore(new ModelInstance(instances), 1331, 3630);
    }

    @Test
    public void test2() throws IOException {
        Globals lua = JsePlatform.standardGlobals();
        LuaJC.install(lua);

        String sourceScript = new String(Files.readAllBytes(Paths.get("resource/test2/source.lua")));
        DslInstance source = new LuaInstance(2, sourceScript, lua);

        String targetScript = new String(Files.readAllBytes(Paths.get("resource/test2/target.lua")));
        DslInstance target = new LuaInstance(2, targetScript, lua);

        DslInstance[] instances = new DslInstance[] { source, target };

        // TODO states and transition count are different from same model in Lambda, to check.
        TestUtil.explore(new ModelInstance(instances), 14, 15);
    }
}
