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

package org.xid.explorer.lambda;

import org.xid.explorer.ResourceResolver;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.dsl.DslRuntime;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * LambdaRuntime is the runtime for Lambda Dsl.
 */
public class LambdaRuntime implements DslRuntime {

    @Override
    public String getId() {
        return "explorer.lambda";
    }

    @Override
    public DslInstance createInstance(DslInstanceDescription description, ResourceResolver resourceResolver) throws Exception {
        ClassLoader loader = LambdaRuntime.class.getClassLoader();
        // TODO checks for classpath add-on with a new ClassLoader

        String className = description.getParameters().get("class");
        if (className == null) throw new Exception("No class parameter provided.");

        String fieldName = description.getParameters().get("field");
        LambdaTransition transition = findTransition(className, fieldName, loader);
        return new LambdaInstance(description.getName(), description.getSize(), transition);
    }

    private LambdaTransition findTransition(String className, String fieldName, ClassLoader loader) throws Exception {
        Class<LambdaTransition> transitionClass = LambdaTransition.class;
        Class<?> clazz = loader.loadClass(className);
        if (fieldName != null) {
            // searches an instantiated static field
            Field field = clazz.getField(fieldName);
            // forces field to be accessible
            field.setAccessible(true);
            //  checks if field is static (already instantiated)
            if (Modifier.isStatic(field.getModifiers()) == false) throw new Exception("Field '" + className + ":" + fieldName +"' isn't static.");
            //  checks if field type is LambdaTransition
            if (transitionClass.isAssignableFrom(field.getType()) == false) throw new Exception("Field '" + className + ":" + fieldName +"' isn't of type "+ transitionClass.getName() +".");
            return transitionClass.cast(field.get(null));

        } else {
            // searches for a class to instantiate
            if (transitionClass.isAssignableFrom(clazz) == false) throw new Exception("Class '" + className + "' isn't of type "+ transitionClass.getName() +".");
            return transitionClass.cast(clazz.newInstance());
        }
    }
}
