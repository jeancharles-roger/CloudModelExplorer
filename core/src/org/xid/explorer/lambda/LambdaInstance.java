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

package org.xid.explorer.lambda;

import org.xid.explorer.Mailboxes;
import org.xid.explorer.dsl.BinaryDslState;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

/**
 * LambdaInstance is a DslInstance that embeds a Java lambda as a next function.
 */
public class LambdaInstance implements DslInstance {

    private final int size;

    private final LambdaTransition transition;

    public LambdaInstance(int size, LambdaTransition transition) {
        this.size = size;
        this.transition = transition;
    }

    @Override
    public DslState createInitialState() {
        return new BinaryDslState(size);
    }

    @Override
    public void next(DslState state, Mailboxes mailboxes) {
        transition.next(state, mailboxes);
    }

}
