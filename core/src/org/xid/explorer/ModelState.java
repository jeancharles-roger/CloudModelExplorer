package org.xid.explorer;

import org.xid.explorer.dsl.DslState;

import java.util.Arrays;

/**
 * Represents a State for an exploration.
 */
public class ModelState {

    public int id = -1;

    public final DslState[] states;

    public ModelState(DslState[] states) {
        this.states = states;
    }

    public ModelState copy(int index, DslState modifiedState) {
        DslState[] statesCopy = Arrays.copyOf(states, states.length);
        statesCopy[index] = modifiedState;
        return new ModelState(statesCopy);
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
