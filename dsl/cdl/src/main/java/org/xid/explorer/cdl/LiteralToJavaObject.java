/*
 * Copyright 2015 to CloudModelExplorer authors
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

package org.xid.explorer.cdl;

import obp.literal.AnyLiteral;
import obp.literal.ArrayLiteral;
import obp.literal.BooleanLiteral;
import obp.literal.ConstantReference;
import obp.literal.IntegerLiteral;
import obp.literal.Literal;
import obp.literal.LiteralField;
import obp.literal.LiteralVisitor;
import obp.literal.RecordLiteral;
import obp.literal.StringLiteral;
import obp.literal.UnionLiteral;
import org.xid.basics.error.DiagnosticUtil;
import org.xid.explorer.ExplorationContext;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Stack;

public class LiteralToJavaObject implements LiteralVisitor {

	/**
	 * <p>Transforms given {@link obp.literal.Literal} to a Java string.
	 * @param literal to transform
	 * @return a Java object
	 */
	public static Object toJava(Literal literal, ExplorationContext context) {
		LiteralToJavaObject transformer = new LiteralToJavaObject(context);
		literal.accept(transformer);
		return transformer.objectStack.pop();
	}
	
	private final ExplorationContext context;
	private final Stack<Object> objectStack = new Stack<>();
	
	private LiteralToJavaObject(ExplorationContext context) {
		this.context = context;
	}
	
	@Override
	public void visitAnyLiteral(AnyLiteral toVisit) {
		throw new IllegalArgumentException("Can't create an 'any' literal.");
	}

	@Override
	public void visitStringLiteral(StringLiteral toVisit) {
		objectStack.push(toVisit.getValue());
	}

	@Override
	public void visitBooleanLiteral(BooleanLiteral toVisit) {
		objectStack.push(toVisit.isValue());
	}

	@Override
	public void visitIntegerLiteral(IntegerLiteral toVisit) {
		objectStack.push(toVisit.getValue());
	}

	@Override
	public void visitConstantReference(ConstantReference toVisit) {
		String name = toVisit.getName();
		String instanceName = null;
		String parameterName = null;
		int dotIndex = name.indexOf('.');
		if (dotIndex >= 0) {
			instanceName = name.substring(0, dotIndex);
			parameterName = name.substring(dotIndex+1, name.length());
		}
		Object value = context.getModelDescription().getParameterValue(instanceName, parameterName, Object.class, null);
		if (value == null) throw new IllegalArgumentException("Constant '"+ name +"' doesn't exist.");
		objectStack.push(value);
	}

	@Override
	public void visitArrayLiteral(ArrayLiteral toVisit) {
		try {
			Object value;
			if ( "integer".equals(toVisit.getTypeName()) ) {
				value = Array.newInstance(int.class, toVisit.getValueCount());
			} else if ( "boolean".equals(toVisit.getTypeName()) ) {
				value = Array.newInstance(boolean.class, toVisit.getValueCount());
			} else if ( "string".equals(toVisit.getTypeName()) ) {
				value = Array.newInstance(String.class, toVisit.getValueCount());
			} else {
				// TODO what about the package ?
				String className =toVisit.getTypeName();
				Class<?> innerType = getClass().getClassLoader().loadClass(className);
		
				value = Array.newInstance(innerType, toVisit.getValueCount());
			}
			
			for ( int i=0; i<toVisit.getValueCount(); i++ ) {
				toVisit.getValue(i).accept(this);
				Array.set(value, i, objectStack.pop());
			}
			
			objectStack.push(value);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Type '"+ toVisit.getTypeName() +"' doesn't exist.");
		}
	}

	@Override
	public void visitLiteralField(LiteralField toVisit) {
		// never called, do nothing
	}

	@Override
	public void visitRecordLiteral(RecordLiteral toVisit) {
		try {
			// TODO what about the package ?
			String className = toVisit.getTypeName();
			Class<?> type = getClass().getClassLoader().loadClass(className);
		
			Object value = type.newInstance();
			
			for ( LiteralField field : toVisit.getFieldList() ) {
				field.getValue().accept(this);
				Object fieldValue = objectStack.pop();
				
				try {
					// TODO does it needs naming
					String fieldName = field.getName();
					Field javaField = type.getField(fieldName);
					javaField.set(value, fieldValue);
					
				} catch (NoSuchFieldException e) {
					throw new IllegalArgumentException("Field '"+ field.getName() +"' doesn't exist in type '"+ toVisit.getTypeName() +"'.");
				}
			}
			
			objectStack.push(value);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Type '"+ toVisit.getTypeName() +"' doesn't exist.");
			
		} catch (IllegalArgumentException e) {
			throw e;

		} catch (Exception e) {
			throw new IllegalArgumentException("Internal error ("+ DiagnosticUtil.createMessage(e) +").");
		}
	}

	@Override
	public void visitUnionLiteral(UnionLiteral toVisit) {
		try {
			// TODO what about the package ?
			String className = toVisit.getTypeName();
			Class<?> unionType = getClass().getClassLoader().loadClass(className);
		
			Class<?> type = null;
			// TODO does it needs naming
			String constrName = toVisit.getName();
			for ( Class<?> oneConstrType : unionType.getClasses() ) {
				if (oneConstrType.getSimpleName().equals(constrName) ) {
					type = oneConstrType;
					break;
				}
			}
			
			if ( type == null ) {
				throw new IllegalArgumentException("Constr '"+ toVisit.getName() +"' doesn't exist in union '"+ toVisit.getTypeName() +"'.");
			}
					
			Object value;
			if ( toVisit.getValue() != null ) {
				toVisit.getValue().accept(this);
				Object fieldValue = objectStack.pop();
				Constructor<?> constructor = findConstructor(type,fieldValue.getClass());
				if ( constructor == null ) {
					throw new IllegalArgumentException("Constr '"+ toVisit.getName() +"' doesn't accept '"+ fieldValue.getClass().getSimpleName() +"' typed argument.");
				}
				value = constructor.newInstance(fieldValue);
				
			} else {
				value = type.newInstance();
			}
			
			objectStack.push(value);
			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Type '"+ toVisit.getTypeName() +"' doesn't exist.");
			
		} catch (IllegalArgumentException e) {
			throw e;
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Internal error ("+ DiagnosticUtil.createMessage(e) +").");
		}
	}
	
	/** Smart constructor search, takes in account subtyping and some primitive types. */
	private Constructor<?> findConstructor(Class<?> type, Class<?> parameterType) {
		for ( Constructor<?> constructor : type.getConstructors() ) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if ( parameterTypes.length == 1 ) {
				Class<?> testedType = parameterTypes[0];
				if ( testedType.isPrimitive() ) {
					if ( testedType == int.class && parameterType == Integer.class ) return constructor;
					if ( testedType == boolean.class && parameterType == Boolean.class ) return constructor;
				} else {
					if ( testedType.isAssignableFrom(parameterType) ) return constructor;
				}
			}
		}
		return null;
	}
	
}
