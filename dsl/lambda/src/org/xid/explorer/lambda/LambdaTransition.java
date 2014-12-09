package org.xid.explorer.lambda;

import org.xid.explorer.dsl.DslState;

/**
 * Created by j5r on 19/11/2014.
 */
@FunctionalInterface
public interface LambdaTransition {
    boolean next(DslState source, DslState target);
}
