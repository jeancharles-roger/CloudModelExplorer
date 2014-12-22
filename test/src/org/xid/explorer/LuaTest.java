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

import org.junit.Rule;
import org.junit.Test;

public class LuaTest {

    @Rule
    public ModelName modelName = new ModelName();

    @Test
    public void test1() throws Exception {
        // TODO transition count is different from same model in Lambda, to check.
        TestUtil.explore("resource/lua/test1/", 1331, 3630);
    }

    @Test
    public void test2() throws Exception {
        // TODO states and transition count are different from same model in Lambda, to check.
        TestUtil.explore("resource/lua/test2/", 14, 15);
    }

}
