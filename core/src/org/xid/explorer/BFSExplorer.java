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

import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslState;
import org.xid.explorer.dsl.DslTransition;
import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.result.ModelExplorationHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j5r on 19/11/2014.
 */
public class BFSExplorer extends AbstractExplorer {

    private List<ModelState> toSee = new ArrayList<>();

    public BFSExplorer(ModelInstance modelInstance) {
        super(modelInstance);
    }

    public BFSExplorer(ModelInstance modelInstance, ModelExplorationHandler explorationHandler) {
        super(modelInstance, explorationHandler);
    }

    @Override
    protected void exploreFrom(ModelState initialState) {

        // find
        DslInstance[] instances = modelInstance.getInstances();
        DslTransition[][] transitions = new DslTransition[instances.length][];
        for (int i = 0; i < instances.length; i++) {
            transitions[i] = instances[i].getTransitions();
        }

        while (toSee.size() > 0) {
            ModelState toExplore = toSee.remove(0);

            Mailboxes mailboxes = toExplore.getMailboxes();

            for (int i = 0; i < instances.length; i++) {
                DslState dslSource = toExplore.getState(i);
                DslState dslTarget = dslSource.copy();
                Mailboxes mailboxesCopy = mailboxes.copy();

                boolean copy = true;
                for (int j = 0; j < transitions[i].length; j++) {
                    copy = false;

                    // computes next
                    transitions[i][j].next(dslTarget, mailboxesCopy);
                    if (dslTarget.equals(dslSource) == false || mailboxesCopy.equals(mailboxes) == false) {
                        // transition changed state, checks if a new model state has been found
                        ModelState target = registerState(toExplore.copy(i, dslTarget, mailboxesCopy));
                        registerTransition(toExplore, target);

                        copy = true;
                    }
                }
            }
        }
    }

    @Override
    protected void newState(ModelState newState) {
        toSee.add(newState);
    }
}
