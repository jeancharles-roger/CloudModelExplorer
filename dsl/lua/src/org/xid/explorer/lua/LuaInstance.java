package org.xid.explorer.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

import java.io.IOException;

/**
 * Created by j5r on 20/11/2014.
 */
public class LuaInstance implements DslInstance {


    private final int stateSize;

    private final String script;

    private final LuaValue function;

    public LuaInstance(int stateSize, String script, Globals lua) throws IOException {
        this.stateSize = stateSize;
        this.script = script;

        StringBuilder functionString = new StringBuilder();
        functionString.append("return function (");
        functionString.append("current");
        functionString.append(");\n");
        functionString.append(script);
        functionString.append("\nend");
        this.function = LuaUtil.parseString(functionString.toString(), lua).call();
    }

    @Override
    public DslState createInitialState() {
        return new DslState(stateSize);
    }

    @Override
    public boolean next(DslState target) {
        LuaUserdata current = LuaValue.userdataOf(target);
        Varargs result = function.call(current);
        return result.toboolean(1);
    }
}
