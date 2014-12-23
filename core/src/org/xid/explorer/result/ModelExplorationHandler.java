/*
 * Copyright 2015 to CloudModelExplorer authors
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

package org.xid.explorer.result;

import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;

/**
 * ModelExplorationStream TODO
 */
public interface ModelExplorationHandler {

    void begin();

    void state(ModelState state);
    void transition(ModelState source, ModelTransition transition, ModelState target);

    void end();

    public static final ModelExplorationHandler EMPTY = new ModelExplorationHandler() {
        @Override
        public void begin() { }

        @Override
        public void state(ModelState state) {}

        @Override
        public void transition(ModelState source, ModelTransition transition, ModelState target) {}

        @Override
        public void end() {}
    };

    public static class ModelExplorationVisitorComposite implements ModelExplorationHandler {

        private final ModelExplorationHandler[] children;

        public ModelExplorationVisitorComposite(ModelExplorationHandler[] children) {
            this.children = children;
        }

        @Override
        public void begin() {
            for (ModelExplorationHandler child : children) child.begin();
        }

        @Override
        public void state(ModelState state) {
            for (ModelExplorationHandler child : children) child.state(state);
        }

        @Override
        public void transition(ModelState source, ModelTransition transition, ModelState target) {
            for (ModelExplorationHandler child : children) child.transition(source, transition, target);
        }

        @Override
        public void end() {
            for (ModelExplorationHandler child : children) child.end();
        }

    }
}
