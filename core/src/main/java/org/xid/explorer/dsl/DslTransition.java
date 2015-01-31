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

import org.xid.explorer.ExplorationContext;
import org.xid.explorer.Mailboxes;

/**
 * Created by j5r on 08/01/2015.
 */
@FunctionalInterface
public interface DslTransition {

    /**
     * Computes next state for instance.
     * @param context
     * @param state a copy of the source state. It's maybe changed by next to create a new state. If no change is made
     *              the explore will consider that the instance has nothing to do.
     * @param mailboxes contents of all mailboxes.
     */
    void next(ExplorationContext context, DslState state, Mailboxes mailboxes);

}
