package org.xid.explorer;

import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j5r on 19/11/2014.
 */
public abstract class AbstractExplorer {

    protected final ModelInstance modelInstance;

    private final Map<ModelState, ModelState> known = new HashMap<>();

    public AbstractExplorer(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public void explore() {
        long start = System.currentTimeMillis();
        ModelState initialState = createInitialState();
        registerState(initialState);
        exploreFrom(initialState);
        long end = System.currentTimeMillis();
        System.out.println("Explored "+ known.size() +" states in "+ (end-start) +"ms.");
    }

    protected abstract void exploreFrom(ModelState initialState);

    protected ModelState createInitialState() {
        DslInstance[] instances = modelInstance.instances;
        DslState[] dslStates = new DslState[instances.length];
        for (int i = 0; i < instances.length; i++) {
            dslStates[i] = instances[i].createInitialState();
        }
        return new ModelState(dslStates);
    }

    protected ModelState registerState(ModelState newState) {
        // searches in known
        ModelState existingState = known.get(newState);
        if (existingState != null) return existingState;

        // doesn't exist, id it and register it.
        newState.id = known.size();

        known.put(newState, newState);
        newState(newState);
        return newState;
    }

    /** This method is called when a new state is registered. */
    protected abstract void newState(ModelState newState);

}
