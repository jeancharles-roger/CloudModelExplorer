package org.xid.explorer;

import org.xid.explorer.dsl.DslState;

import java.util.Arrays;

/**
 * Represents a State for an exploration.
 */
public class ExplorationState {

    public int id = -1;

    public final DslState[] states;

    public ExplorationState(DslState[] states) {
        this.states = states;
    }

    public ExplorationState copy(int index, DslState modifiedState) {
        DslState[] statesCopy = Arrays.copyOf(states, states.length);
        statesCopy[index] = modifiedState;
        return new ExplorationState(statesCopy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplorationState state = (ExplorationState) o;

        if (!Arrays.equals(states, state.states)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(states);
    }
}
