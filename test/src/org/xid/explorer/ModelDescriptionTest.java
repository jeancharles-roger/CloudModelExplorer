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

package org.xid.explorer;

import org.junit.Assert;
import org.junit.Test;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.model.ModelDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by j5r on 12/01/2015.
 */
public class ModelDescriptionTest {

    private List<DslInstanceDescription> createInstanceDescriptionList(int count) {
        List<DslInstanceDescription> instances = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            instances.add(createInstanceDescription("instance" + i, i*5));
        }
        return instances;
    }

    private DslInstanceDescription createInstanceDescription(String name, int parameterCount) {
        DslInstanceDescription description = new DslInstanceDescription();
        description.setName(name);

        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < parameterCount; i++) {
            parameters.put("p" + i, i);
        }
        description.setInstanceParameters(parameters);
        return description;
    }

    @Test
    public void testGetInstanceId() {
        final int m = 10;
        int n = 100_000;

        ModelDescription description = new ModelDescription();
        description.setInstances(createInstanceDescriptionList(m));

        int count = 0;
        long start = System.currentTimeMillis();
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < i * 5; j++) {
                    int value = description.getParameterValue("instance"+ i, "p"+j, Integer.class, -1);
                    Assert.assertEquals(j, value);
                    count += 1;
                }
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("Get integer parameter value " + count + " in " + (end-start) + " ms.");
    }
}
