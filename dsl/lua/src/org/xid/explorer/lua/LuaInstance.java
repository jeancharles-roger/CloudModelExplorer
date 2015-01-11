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

package org.xid.explorer.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.xid.explorer.dsl.BinaryDslState;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;
import org.xid.explorer.dsl.DslTransition;

import java.io.IOException;

/**
 * Created by j5r on 20/11/2014.
 */
public class LuaInstance implements DslInstance {

    private final String name;

    private final int stateSize;

    private final String script;

    private final LuaValue function;

    public LuaInstance(String name, int stateSize, String script, Globals lua) throws IOException {
        this.name = name;
        this.stateSize = stateSize;
        this.script = script;

        StringBuilder functionString = new StringBuilder();
        functionString.append("return function (");
        functionString.append("state, mailboxes");
        functionString.append(");\n");
        functionString.append(script);
        functionString.append("\nend");
        this.function = LuaUtil.parseString(functionString.toString(), lua).call();
    }

    @Override
    public DslState createInitialState() {
        return new BinaryDslState(stateSize);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DslTransition[] getTransitions() {
        return new DslTransition[] {
                (context, state, mailboxes) -> function.call(CoerceJavaToLua.coerce(state), CoerceJavaToLua.coerce(mailboxes))
        };
    }
}
