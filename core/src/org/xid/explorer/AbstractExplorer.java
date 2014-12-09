package org.xid.explorer;

import org.xid.explorer.dsl.DslState;
import org.xid.explorer.dsl.DslInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j5r on 19/11/2014.
 */
public abstract class AbstractExplorer {

    protected final DslInstance[] instances;

    private final Map<ExplorationState, ExplorationState> known = new HashMap<>();

    public AbstractExplorer(DslInstance[] instances) {
        this.instances = instances;
    }

    public void explore() {
        long start = System.currentTimeMillis();
        ExplorationState initialState = createInitialState();
        registerState(initialState);
        exploreFrom(initialState);
        long end = System.currentTimeMillis();
        System.out.println("Explored "+ known.size() +" states in "+ (end-start) +"ms.");
    }

    protected abstract void exploreFrom(ExplorationState initialState);

    protected ExplorationState createInitialState() {
        DslState[] dslStates = new DslState[instances.length];
        for (int i = 0; i < instances.length; i++) {
            dslStates[i] = instances[i].createInitialState();
        }

        return new ExplorationState(dslStates);
    }

    protected ExplorationState registerState(ExplorationState newState) {
        // searches in known
        ExplorationState existingState = known.get(newState);
        if (existingState != null) return existingState;

        // doesn't exist, id it and register it.
        newState.id = known.size();

        known.put(newState, newState);
        newState(newState);
        return newState;
    }

    /** This method is called when a new state is registered. */
    protected abstract void newState(ExplorationState newState);

}
