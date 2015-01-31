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

import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.model.ModelState;
import org.xid.explorer.model.ModelTransition;

import java.io.PrintWriter;

/**
 * ModelExplorationDotPrinter generate a dot format from the exploration result.
 */
public class ModelResultDotPrinter implements ModelResultHandler {

    private final ModelInstance modelInstance;

    private final PrintWriter writer;

    private final boolean detailed;

    public ModelResultDotPrinter(ModelInstance modelInstance, PrintWriter writer, boolean detailed) {
        this.modelInstance = modelInstance;
        this.writer = writer;
        this.detailed = detailed;
    }

    @Override
    public void begin() {
        writer.println("digraph exploration {");
    }

    @Override
    public void state(ModelState state) {
        writer.print("\t ");
        writer.print(state.getId());

        if (detailed) {
            StringBuilder stateText = new StringBuilder();
            state.printOn(modelInstance, stateText);
            String label = stateText.toString().replaceAll("\n", "\\\\n");
            writer.print(" [label=\"" + label + "\"]");
        }

        writer.println(";");
    }

    @Override
    public void transition(ModelTransition transition) {
        writer.println("\t " + transition.getSourceId() + " -> "+ transition.getTargetId() + ";");
    }

    @Override
    public void end() {
        writer.println("}");
        writer.close();
    }

}
