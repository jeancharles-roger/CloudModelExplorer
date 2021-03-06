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
import org.xid.explorer.model.MailboxDescription;
import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;
import org.xid.explorer.result.ModelExploration;
import org.xid.explorer.result.ModelExploration.CompletionStatus;
import org.xid.explorer.result.ModelResultDescription;
import org.xid.explorer.result.ModelResultHandler;
import org.xid.explorer.result.ModelResultUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by j5r on 19/11/2014.
 */
public abstract class AbstractExplorer implements ExplorationContext {

    protected final ModelDescription modelDescription;

    protected final ResourceResolver resourceResolver;

    protected ModelInstance modelInstance;

    private final Map<ModelState, ModelState> known = new HashMap<>();

    private ModelResultHandler explorationHandler;

    private int transitionCount = 0;

    public AbstractExplorer(ModelDescription modelDescription, ResourceResolver resourceResolver) {
        this.modelDescription = modelDescription;
        this.resourceResolver = resourceResolver;
        this.explorationHandler = ModelResultHandler.EMPTY ;
    }

    public void initialize(List<ModelResultDescription> resultDescriptions) throws Exception {
        modelInstance = ModelInstance.load(modelDescription, resourceResolver);
        for (DslInstance instance : modelInstance.getInstances()) {
            instance.initialize(this);
        }

        explorationHandler = ModelResultUtil.loadModelExplorationHandler(this, resultDescriptions);
    }

    @Override
    public final ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public ModelExploration explore(ActionMonitor monitor) throws Exception {
        long start = System.currentTimeMillis();

        ModelState initialState = createInitialState();

        explorationHandler.begin();

        registerState(initialState);
        exploreFrom(initialState);

        explorationHandler.end();

        long end = System.currentTimeMillis();

        return new ModelExploration(modelInstance.getDescription(), CompletionStatus.complete, (end-start), known.size(), transitionCount);
    }

    protected abstract void exploreFrom(ModelState initialState);

    protected ModelState createInitialState() {
        // creates DslState for each instance
        DslInstance[] instances = modelInstance.getInstances();
        DslState[] dslStates = new DslState[instances.length];
        for (int i = 0; i < instances.length; i++) {
            dslStates[i] = instances[i].createInitialState();
        }

        // initializes mailboxes
        Mailboxes mailboxes = null;
        List<MailboxDescription> mailboxDescriptions = modelInstance.getDescription().getMailboxes();
        if (mailboxDescriptions.size() > 0) {
            mailboxes = new Mailboxes(new String[mailboxDescriptions.size()][]);
        }

        return new ModelState(dslStates, mailboxes);
    }

    protected void registerTransition(ModelState source, ModelState target) {
        transitionCount += 1;

        // sends transition to exploration handler
        // XXX creates a new object here, maybe reuse could be beneficial
        explorationHandler.transition(new ModelTransition(source.getId(), null, target.getId()));
    }

    protected ModelState registerState(ModelState newState) {
        // searches in known
        ModelState existingState = known.get(newState);
        if (existingState != null) return existingState;

        // doesn't exist, id it and register it.
        newState.setId(known.size());

        known.put(newState, newState);
        newState(newState);

        // sends new state to exploration handler.
        explorationHandler.state(newState);

        return newState;
    }

    /** This method is called when a new state is registered. */
    protected abstract void newState(ModelState newState);

}
