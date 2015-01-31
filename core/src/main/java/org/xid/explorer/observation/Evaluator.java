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

package org.xid.explorer.observation;

import org.xid.explorer.ExplorationContext;
import org.xid.explorer.model.ModelState;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Evaluator defines an interface to evaluate an expression over a Java object.
 */
@FunctionalInterface
public interface Evaluator {

    /**
     * Does given object matches this.
     * @param context exploration context.
     * @param toMatch object to match
     * @return true if object matches this, false otherwise.
     */
    Object evaluate(ExplorationContext context, Object toMatch);

    public static Evaluator constant(Object constant) {
        return (context, toMatch) -> constant;
    }

    /** This Evaluator returns the value passed as argument */
    public static Evaluator leaf() {
        return (context, toMatch) -> toMatch;
    }

    public static Evaluator not(Evaluator child) {
        return (context, toMatch) -> (child.evaluate(context, toMatch) == Boolean.FALSE);
    }

    public static Evaluator and(Evaluator... children) {
        return (context, toMatch) -> {
            for (int i = 0; i < children.length; i++) {
                if (children[i].evaluate(context, toMatch) == Boolean.FALSE) return Boolean.FALSE;
            }
            return Boolean.TRUE;
        };
    }

    public static Evaluator or(Evaluator... children) {
        return (context, toMatch) -> {
            for (int i = 0; i < children.length; i++) {
                if (children[i].evaluate(context, toMatch) == Boolean.TRUE) return Boolean.TRUE;
            }
            return Boolean.FALSE;
        };
    }

    public static Evaluator greaterThan(Evaluator left, Evaluator right) {
        return (context, toMatch) -> {
            int leftValue = ((Number) left.evaluate(context, toMatch)).intValue();
            int rightValue = ((Number) right.evaluate(context, toMatch)).intValue();
            return leftValue > rightValue;
        };
    }


    public static Evaluator greaterOrEqualsThan(Evaluator left, Evaluator right) {
        return (context, toMatch) -> {
            int leftValue = ((Number) left.evaluate(context, toMatch)).intValue();
            int rightValue = ((Number) right.evaluate(context, toMatch)).intValue();
            return leftValue >= rightValue;
        };
    }


    public static Evaluator lesserThan(Evaluator left, Evaluator right) {
        return (context, toMatch) -> {
            int leftValue = ((Number) left.evaluate(context, toMatch)).intValue();
            int rightValue = ((Number) right.evaluate(context, toMatch)).intValue();
            return leftValue < rightValue;
        };
    }


    public static Evaluator lesserOrEqualsThan(Evaluator left, Evaluator right) {
        return (context, toMatch) -> {
            int leftValue = ((Number) left.evaluate(context, toMatch)).intValue();
            int rightValue = ((Number) right.evaluate(context, toMatch)).intValue();
            return leftValue <= rightValue;
        };
    }

    public static Evaluator equals(Evaluator left, Evaluator right) {
        return (context, toMatch) -> {
            Object leftValue = left.evaluate(context, toMatch);
            Object rightValue = right.evaluate(context, toMatch);
            return leftValue == null ? rightValue == null : leftValue.equals(rightValue);
        };
    }

    public static Evaluator array(int index, Evaluator child) {
        return (context, toMatch) -> child.evaluate(context, Array.get(toMatch, index));
    }

    public static Evaluator arrayLength(int index, Evaluator child) {
        return (context, toMatch) -> child.evaluate(context, Array.getLength(toMatch));
    }

    public static Evaluator type(Class<?> type) {
        return (context, toMatch) -> type.isInstance(toMatch);
    }

    public static Evaluator field(Field field, Evaluator child) {
        return (context, toMatch) -> {
            try {
                return child.evaluate(context, field.get(toMatch));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Evaluator instance(int id, Evaluator child) {
        return (context, toMatch) -> child.evaluate(context, ((ModelState) toMatch).getState(id));
    }

    public static Evaluator matcher(Evaluator value, Matcher matcher) {
        return (context, toMatch) -> matcher.match(context, value.evaluate(context, value));
    }
}
