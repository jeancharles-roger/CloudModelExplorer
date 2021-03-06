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

package org.xid.explorer.dsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* DslInstanceDescription contains information to create a DslInstance.
*/
public class DslInstanceDescription {

    private String dsl;

    private String name;

    private int size;

    private List<String> resources = new ArrayList<>();

    private Map<String, String> dslParameters = new HashMap<>();

    private Map<String, Object> instanceParameters;

    public String getDsl() {
        return dsl;
    }

    public void setDsl(String dsl) {
        this.dsl = dsl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public Map<String, String> getDslParameters() {
        return dslParameters;
    }

    public void setDslParameters(Map<String, String> dslParameters) {
        this.dslParameters = dslParameters;
    }

    public Map<String, Object> getInstanceParameters() {
        return instanceParameters;
    }

    public void setInstanceParameters(Map<String, Object> instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    public Object getInstanceParameter(String name) {
        return instanceParameters.get(name);
    }
}
