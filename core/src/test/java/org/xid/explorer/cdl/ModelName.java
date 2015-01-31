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

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
* Get the class and method name to construct model id.
*/
class ModelName extends TestWatcher {
    private String name;

    @Override
    protected void starting(Description d) {
        String className = d.getClassName();
        int index = className.lastIndexOf('.');
        this.name = className.substring(index >= 0 ? index+1 : 0) + "." + d.getMethodName();
    }

    public String getName() {
        return name;
    }
}
