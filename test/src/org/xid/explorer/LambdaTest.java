package org.xid.explorer;

import org.junit.Test;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;

public class LambdaTest {

    @Test
    public void test1() {
        DslInstance instance = new DslInstance() {
            @Override
            public DslState createInitialState() {
                return new DslState(2);
            }

            @Override
            public boolean next(DslState target) {
                int count = target.getInt(0);
                int newCount = count < 10 ? count + 1 : 0;
                target.setInt(0, newCount);
                return true;
            }
        };
        DslInstance[] instances = new DslInstance[] {
                instance, instance, instance
        };

        BFSExplorer explorer = new BFSExplorer(instances);
        explorer.explore();
    }
}
