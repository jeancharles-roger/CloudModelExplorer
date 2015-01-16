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

import java.io.IOException;

/**
 * ModelResultHandler TODO
 */
public interface ModelResultHandler {

    void begin() throws IOException;

    void state(ModelState state);
    void transition(ModelTransition transition);

    void end() throws IOException;

    public static final ModelResultHandler EMPTY = new ModelResultHandler() {
        @Override
        public void begin() { }

        @Override
        public void state(ModelState state) {}

        @Override
        public void transition(ModelTransition transition) {}

        @Override
        public void end() {}
    };

    public static class ModelResultVisitorComposite implements ModelResultHandler {

        private final ModelResultHandler[] children;

        public ModelResultVisitorComposite(ModelResultHandler[] children) {
            this.children = children;
        }

        @Override
        public void begin() throws IOException{
            for (ModelResultHandler child : children) child.begin();
        }

        @Override
        public void state(ModelState state) {
            for (ModelResultHandler child : children) child.state(state);
        }

        @Override
        public void transition(ModelTransition transition) {
            for (ModelResultHandler child : children) child.transition(transition);
        }

        @Override
        public void end() throws IOException {
            for (ModelResultHandler child : children) child.end();
        }

    }
}
