package org.xid.explorer;

import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.lambda.LambdaInstance;
import org.xid.explorer.lua.LuaInstance;
import org.xid.explorer.lua.StateLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MixedTest {

    @Test
    public void test1() throws IOException {

        // Lambda instance
        DslInstance lambdaInstance = new LambdaInstance(2, (target) -> {
            int count = target.getInt(0);
            int newCount = count < 10 ? count + 1 : 0;
            target.setInt(0, newCount);
            return true;
        });

        // Lua instance
        Globals lua = JsePlatform.standardGlobals();
        new StateLib().call(lua);

        String script = new String(Files.readAllBytes(Paths.get("resource/test1.lua")));
        DslInstance luaInstance = new LuaInstance(2, script, lua);


        DslInstance[] instances = new DslInstance[] {
                luaInstance, lambdaInstance
        };

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }
}
