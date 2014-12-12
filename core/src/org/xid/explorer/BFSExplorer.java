package org.xid.explorer;

import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j5r on 19/11/2014.
 */
public class BFSExplorer extends AbstractExplorer {

    private List<ExplorationState> toSee = new ArrayList<>();

    public BFSExplorer(DslInstance[] instances) {
        super(instances);
    }

    @Override
    protected void exploreFrom(ExplorationState initialState) {

        while (toSee.size() > 0) {
            ExplorationState toExplore = toSee.remove(0);

            for (int i = 0; i < instances.length; i++) {
                DslState target = toExplore.states[i].copy();
                if (instances[i].next(target)) {
                    registerState(toExplore.copy(i, target));
                }
            }
        }
    }

    @Override
    protected void newState(ExplorationState newState) {
        toSee.add(newState);
    }
}
