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

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.LuajavaLib;
import org.xid.explorer.dsl.DslState;

/**
 * <p>Interpreter Lib for Lua runtime.</p>
 * @author Jean-Charles Roger (jean-charles.roger@ensta-bretagne.fr)
 *
 */
public class StateLib extends LuajavaLib {

	@Override
	public LuaValue call(LuaValue env) {
		final LuaTable state = new LuaTable();
		setInt(state);
		getInt(state);

		env.set("state", state);
		return state;
	}

	private void getInt(final LuaTable state) {
		state.set("getInt", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue luaState, LuaValue index) {
				DslState state = (DslState) luaState.touserdata(DslState.class);
				if (state == null) return LuaValue.NIL;
				return LuaValue.valueOf(state.getInt(index.toint()));
			}
		});
	}

	private void setInt(final LuaTable state) {
		state.set("setInt", new ThreeArgFunction() {
			@Override
			public LuaValue call(LuaValue luaState, LuaValue index, LuaValue value) {
				DslState state = (DslState) luaState.touserdata(DslState.class);
				if (state == null) return LuaValue.NIL;
				state.setInt(index.toint(), value.toint());
				return LuaValue.NIL;
			}
		});
	}
}
