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

package org.xid.explorer.dsl;

import org.xid.explorer.ExplorationContext;

import java.io.IOException;

/**
 * A DslInstance represents a behavior, it's a transition function from a source DslState to a target one.
 * All DslInstance act independently from the others.
 */
public interface DslInstance {

    /**
     * Creates the initial state for the instance.
     * @return a new DslState considered as the initial instance state.
     */
    DslState createInitialState();

    /**
     * Initialize the instance.
     * @param context exploration context.
     */
    void initialize(ExplorationContext context) throws IOException;

    /**
     * @return the name of the instance.
     */
    String getName();

    DslTransition[] getTransitions();

}
