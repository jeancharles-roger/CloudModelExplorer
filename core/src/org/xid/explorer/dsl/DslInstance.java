package org.xid.explorer.dsl;

/**
 * Created by j5r on 19/11/2014.
 */
public interface DslInstance {

    DslState createInitialState();

    boolean next(DslState source, DslState target);

}
