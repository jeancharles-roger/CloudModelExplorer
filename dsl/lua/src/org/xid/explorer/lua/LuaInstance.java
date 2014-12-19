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
import org.xid.explorer.Mailboxes;
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
    public void next(DslState state, Mailboxes mailboxes) {
        function.call(LuaValue.userdataOf(state));
    }
}
