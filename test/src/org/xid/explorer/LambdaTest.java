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

package org.xid.explorer;

import org.junit.Test;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;
import org.xid.explorer.lambda.LambdaInstance;

public class LambdaTest {

    /**
     * Simple test with a basic lambda.
     */
    @Test
    public void test0() {
        DslInstance instance = new LambdaInstance(2, (DslState state) -> {
            int count = state.getInt(0);
            int newCount = count < 2 ? count + 1 : 0;
            state.setInt(0, newCount);
        });
        DslInstance[] instances = new DslInstance[] {instance};

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }

    /**
     * Simple test with a basic lambda and several instances
     */
    @Test
    public void test1() {
        DslInstance instance = new LambdaInstance(2, (DslState state) -> {
            int count = state.getInt(0);
            int newCount = count < 10 ? count + 1 : 0;
            state.setInt(0, newCount);
        });
        DslInstance[] instances = new DslInstance[] { instance, instance, instance};

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }

    private static int countMethod(int count, int max) {
        return count < max ? count + 1 : 0;
    }

    /**
     * Test with an existing transition to apply from the instance.
     */
    @Test
    public void test2() {
        DslInstance instance = new LambdaInstance(2, (DslState state) -> {
            int count = state.getInt(0);
            int newCount = countMethod(count, 10);
            state.setInt(0, newCount);
        });

        DslInstance[] instances = new DslInstance[] { instance, instance, instance };

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }
}
