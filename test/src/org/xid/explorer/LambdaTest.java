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
import org.xid.explorer.lambda.LambdaInstance;

public class LambdaTest {

    /**
     * Simple test with a basic lambda.
     */
    @Test
    public void test0() {
        DslInstance instance = new LambdaInstance(2, (state, mailboxes) -> {
            int count = state.getInt(0);
            int newCount = count < 2 ? count + 1 : 0;
            state.setInt(0, newCount);
        });
        DslInstance[] instances = new DslInstance[] {instance};

        TestUtil.explore(new ModelInstance(instances), 3, 3);
    }

    /**
     * Simple test with a basic lambda and several instances
     */
    @Test
    public void test1() {
        DslInstance instance = new LambdaInstance(2, (state, mailboxes) -> {
            int count = state.getInt(0);
            int newCount = count < 10 ? count + 1 : 0;
            state.setInt(0, newCount);
        });
        DslInstance[] instances = new DslInstance[] { instance, instance, instance};

        TestUtil.explore(new ModelInstance(instances), 1331, 3993);
    }

    private static int countMethod(int count, int max) {
        return count < max ? count + 1 : 0;
    }

    /**
     * Test with an existing transition to apply from the instance.
     */
    @Test
    public void test2() {
        DslInstance instance = new LambdaInstance(2, (state, mailboxes) -> {
            int count = state.getInt(0);
            int newCount = countMethod(count, 10);
            state.setInt(0, newCount);
        });

        DslInstance[] instances = new DslInstance[] { instance, instance, instance };

        TestUtil.explore(new ModelInstance(instances), 1331, 3993);
    }

    /**
     * Simple test with a basic lambda and 2 instances that communicates using a mailbox.
     */
    @Test
    public void test3() {
        DslInstance instanceSource = new LambdaInstance(2, (state, mailboxes) -> {
            if (mailboxes.getMailboxesCount() == 0) {
                int index = mailboxes.createMailbox();
                mailboxes.addLast(index, "start");
                state.setInt(0, index);
            } else {
                int index = state.getInt(0);
                String result = mailboxes.removeFirstIf(index, (message) -> "end".equals(message));
                if (result != null) mailboxes.addLast(index, "start");
            }
        });
        DslInstance instanceTarget = new LambdaInstance(2, (state, mailboxes) -> {
            int count = state.getInt(0);
            if (count == 0) {
                if (mailboxes.getMailboxesCount() > 0) {
                    String result = mailboxes.removeFirstIf(0, (message) -> "start".equals(message));
                    if (result != null) count = 1;
                }
            } else if (count >= 10) {
                count = 0;
                mailboxes.addLast(0, "end");
            } else {
                count = count + 1;
            }
            state.setInt(0, count);
        });

        DslInstance[] instances = new DslInstance[] { instanceSource, instanceTarget };

        TestUtil.explore(new ModelInstance(instances), 13, 13);
    }

}
