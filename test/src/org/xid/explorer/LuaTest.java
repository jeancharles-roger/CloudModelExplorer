package org.xid.explorer;

import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.lua.LuaInstance;
import org.xid.explorer.lua.StateLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LuaTest {

    @Test
    public void test1() throws IOException {
        Globals lua = JsePlatform.standardGlobals();
        lua.load(new StateLib());

        String script = new String(Files.readAllBytes(Paths.get("resource/test1.lua")));
        DslInstance instance = new LuaInstance(4, script, lua);
        DslInstance[] instances = new DslInstance[] {
                instance //, instance, instance
        };

        BFSExplorer explorer = new BFSExplorer(instances);
        explorer.explore();
    }
}
