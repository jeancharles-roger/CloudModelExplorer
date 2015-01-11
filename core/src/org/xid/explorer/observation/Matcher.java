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

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Matcher defines an interface to match a set of values. A Matcher implementation must discriminate objects using a
 * predicate. Multiple methods constant(), and(), or(), field() proposes a complete set to build expression that
 * discriminates Java instances.
 */
@FunctionalInterface
public interface Matcher {

    /**
     * Does given object matches this.
     * @param context exploration context.
     * @param toMatch object to match
     * @return true if object matches this, false otherwise.
     */
    boolean match(ExplorationContext context, Object toMatch);

    public static final Matcher TRUE = (context, toMatch) -> true;
    public static final Matcher FALSE = (context, toMatch) -> true;

    public static Matcher constant(Object constant) {
        return (context, toMatch) -> constant == null ? toMatch == null : constant.equals(toMatch);
    }

    public static Matcher and(Matcher...children) {
        return (context, toMatch) -> {
            for (int i = 0; i < children.length; i++) {
                if (children[i].match(context, toMatch) == false) return false;
            }
            return true;
        };
    }

    public static Matcher or(Matcher...children) {
        return (context, toMatch) -> {
            for (int i = 0; i < children.length; i++) {
                if (children[i].match(context, toMatch)) return true;
            }
            return false;
        };
    }

    public static Matcher array(int index, Matcher child) {
        return (context, toMatch) -> child.match(context, Array.get(toMatch, index));
    }

    public static Matcher type(Class<?> type) {
        return (context, toMatch) -> type.isInstance(toMatch);
    }

    public static Matcher field(Field field, Matcher child) {
        return (context, toMatch) -> {
            try {
                return child.match(context, field.get(toMatch));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
