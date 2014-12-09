package org.xid.explorer.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Set of Lua utility methods.</p>
 * 
 * @author Jean-Charles Roger (jean-charles.roger@ensta-bretagne.fr)
 *
 */
public class LuaUtil {

	public static LuaValue evalExpression(String expression, Globals globals) throws IOException {
		return evalExpression(expression, LuaValue.class, globals);
	}
	
	/**
	 * <p>Evaluates the given string as a Lua expression.</p> 
	 * @param expression the expression to evaluate.
	 * @param type the class of expected result.
	 * @param globals lua values
	 * @return the {@link LuaValue} for the evaluated result.
	 * @throws IOException if expression isn't sound.
	 */
	public static <T extends LuaValue> T evalExpression(String expression, Class<T> type, Globals globals) throws IOException {
		// constructs block.
		final String script = "return " + expression;
		
		// parses block.
		final InputStream stream = new ByteArrayInputStream(script.toString().getBytes());
		Prototype prototype = globals.compilePrototype(stream, script);
		final LuaFunction block =  globals.loader.load(prototype, script, globals);

		// evaluates block
		final LuaValue value = block.call();
		return type.cast(value);
	}
	
	/**
	 * <p>Parses script.</p>
	 * @param script Lua script.
	 * @param globals lua values
	 * @return a {@link LuaValue} representing the script.
	 * @throws IOException if script isn't sound Lua script.
	 */
	public static LuaFunction parseString(String script, Globals globals) throws IOException {
		final InputStream stream = new ByteArrayInputStream(script.getBytes());
		Prototype prototype = globals.compilePrototype(stream, script);
		return globals.loader.load(prototype, script, globals);
	}
	
	/**
	 * <p>Parses a file as a script.</p>
	 * @param file Lua file.
	 * @param globals lua values
	 * @return a {@link LuaValue} representing the script.
	 * @throws IOException if file contents isn't sound Lua script.
	 */
	public static LuaFunction parseFile(File file, Globals globals) throws IOException {
		final InputStream stream = new BufferedInputStream(new FileInputStream(file));
		Prototype prototype = globals.compilePrototype(stream, file.getName());
		return globals.loader.load(prototype, file.getName(), globals);
	}
	
	/**
	 * <p>Parses a resource as a script.</p>
	 * @param name Lua resource name.
	 * @param globals lua values
	 * @return a {@link LuaValue} representing the script.
	 * @throws IOException if contents isn't sound Lua script.
	 */
	public static LuaFunction parseResource(String name, Globals globals) throws IOException {
		final InputStream stream = LuaUtil.class.getClassLoader().getResourceAsStream(name);
		if ( stream == null ) {
			throw new IOException("Resource '"+ name +"' can't be found.");
		}
		Prototype prototype = globals.compilePrototype(stream, name);
		return globals.loader.load(prototype, name, globals);
	}

	public static LuaValue[] deepCopy(LuaValue[] values) {
		if ( values == null ) return null;
		
		final LuaValue[] copy = new LuaValue[values.length];
		for ( int i=0; i< values.length; i++ ) {
			copy[i] = LuaUtil.deepCopy(values[i]);
		}
		return copy;
	}
	
	public static LuaValue deepCopy(LuaValue value) {
		// gets type
		final int type = value.type();
		// writes value depending on type
		switch ( type ) {
		// no need to copy there are immutable objects.
		case LuaValue.TNIL:
		case LuaValue.TBOOLEAN:
		case LuaValue.TINT:
		case LuaValue.TNUMBER:
		case LuaValue.TSTRING:
			return value;

		case LuaValue.TTABLE:
			// creates copy table
			final LuaTable copy = new LuaTable();
			Varargs entry = value.next(LuaValue.NIL);
			while (entry.arg1().isnil() == false ) {
				final LuaValue key = entry.arg1();
				copy.set(deepCopy(key), deepCopy(entry.arg(2)));
				entry = value.next(key);
			}
			copy.setmetatable(value.getmetatable());
			return copy;

		default:
			throw new IllegalArgumentException("Can't deep copy Lua values of type '"+ value.typename() +"'.");
		}
	}
	
	public static int deepHashCode(LuaValue[] values) {
		if ( values == null ) return 0;

		int code = 17;
		for ( int i=0; i< values.length; i++ ) {
			code += 37 * LuaUtil.deepHashCode(values[i]);
		}
		return code;
	}
	
	public static int deepHashCode(LuaValue value) {
		// gets type
		final int type = value.type();
		// writes value depending on type
		switch ( type ) {
		// no need to copy there are immutable objects.
		case LuaValue.TNIL:
			return 0;
		case LuaValue.TBOOLEAN:
			return value.toboolean() ? 2 : 1;
			
		case LuaValue.TINT:
		case LuaValue.TNUMBER:
			return value.toint();
			
		case LuaValue.TSTRING:
			return value.tostring().hashCode();
			
		case LuaValue.TTABLE:
			// creates copy table
			int code = 17;

			Varargs entry = value.next(LuaValue.NIL);
			while (entry.arg1().isnil() == false ) {
				final LuaValue key = entry.arg1();
				code += 37 * deepHashCode(key);
				code += 37 * deepHashCode(entry.arg(2));
				entry = value.next(key);
			}
			return code;
			
		default:
			throw new IllegalArgumentException("Can't get hash code of Lua values of type '"+ value.typename() +"'.");
		}
		
	}
	
	public static boolean deepEquals(LuaValue[] me, LuaValue[] you) {
		if ( me == null  ) return you == null;
		if ( you == null ) return false;
		if ( me.length != you.length ) return false;
		for ( int i=0; i< me.length; i++ ) {
			if ( deepEquals(me[i], you[i]) == false ) return false;
		}
		return true;
	}

	public static boolean deepEquals(LuaValue me, LuaValue you) {
		// gets type
		final int meType = me.type();
		final int youType = you.type();
		
		if ( meType != youType ) return false;
		
		// writes value depending on type
		switch ( meType ) {
		// no need to copy there are immutable objects.
		case LuaValue.TNIL:
			return true;
		case LuaValue.TBOOLEAN:
			return me.toboolean() == you.toboolean();
			
		case LuaValue.TINT:
		case LuaValue.TNUMBER:
			return me.toint() == you.toint();
			
		case LuaValue.TSTRING:
			return me.tostring() == you.tostring();
			
		case LuaValue.TTABLE:
			if ( me.length() != you.length() ) return false;

			Varargs meEntry = me.next(LuaValue.NIL);
			Varargs youEntry = you.next(LuaValue.NIL);
			while (meEntry.arg1().isnil() == false ) {
				final LuaValue meKey = meEntry.arg1();
				final LuaValue youKey = youEntry.arg1();
				if ( deepEquals(meKey, youKey) == false ) {
					return false;
				}
				
				if ( deepEquals(meEntry.arg(2), youEntry.arg(2)) == false ) {
					return false;
				}
				
				meEntry = me.next(meKey);
				youEntry = you.next(youKey);
			}
			return true;
			
		default:
			return me == you;
		}
		
	}
	

	/**
	 * <p>Prints {@link LuaValue} as string.</p>
	 * @param value to print
	 * @return a string presenting the value.
	 */
	public static String toString(LuaValue value) {
		if (value == null ) return "nil";
		
		switch (value.type()) {
		case LuaValue.TTABLE:
			final LuaTable table = (LuaTable) value;

			final StringBuilder text = new StringBuilder();
			text.append("{");
			Varargs entry = table.next(LuaValue.NIL);
			while (entry.arg1().isnil() == false ) {
				if ( text.length() > 1 ) text.append(",");
				final LuaValue key = entry.arg1();
				// writes key
				text.append(toString(key));
				text.append("=>");
				// writes value
				text.append(toString(entry.arg(2)));
				
				entry = table.next(key);
			}
			text.append("}");
			return text.toString();
		default:
			return value.tojstring();
		}
	}
}

