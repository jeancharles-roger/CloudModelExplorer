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

public class LambdaTest {

    @Test
    public void test1() {
        DslInstance instance = new DslInstance() {
            @Override
            public DslState createInitialState() {
                return new DslState(2);
            }

            @Override
            public boolean next(DslState target) {
                int count = target.getInt(0);
                int newCount = count < 10 ? count + 1 : 0;
                target.setInt(0, newCount);
                return true;
            }
        };
        DslInstance[] instances = new DslInstance[] {
                instance, instance, instance
        };

        BFSExplorer explorer = new BFSExplorer(new ModelInstance(instances));
        explorer.explore();
    }
}
