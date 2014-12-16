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

import org.xid.explorer.dsl.DslState;

import java.util.Arrays;

/**
 * Represents a State for an exploration.
 */
public class ModelState {

    private int id = -1;

    private final DslState[] states;

    public ModelState(DslState[] states) {
        this.states = states;
    }

    public ModelState copy(int index, DslState modifiedState) {
        DslState[] statesCopy = Arrays.copyOf(states, states.length);
        statesCopy[index] = modifiedState;
        return new ModelState(statesCopy);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DslState getState(int index) {
        return states[index];
    }

    public DslState[] getStates() {
        return states;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelState state = (ModelState) o;

        if (!Arrays.equals(states, state.states)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(states);
    }
}
