package org.xid.explorer.lambda;

import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

/**
 * Created by j5r on 19/11/2014.
 */
public class LambdaInstance implements DslInstance {

    private final LambdaTransition transition;

    public LambdaInstance(LambdaTransition transition) {
        this.transition = transition;
    }

    @Override
    public DslState createInitialState() {
        return new DslState(4);
    }

    @Override
    public boolean next(DslState target) {
        return transition.next(target);
    }
}
