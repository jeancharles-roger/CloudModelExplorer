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
