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

import java.io.PrintWriter;

/**
 * Created by j5r on 23/12/2014.
 */
public class ModelExplorationDotPrinter implements ModelExplorationHandler {

    private final PrintWriter writer;

    private final boolean detailed;

    public ModelExplorationDotPrinter(PrintWriter writer, boolean detailed) {
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
            String label = state.toString().replaceAll("\n", "\\\\n");
            writer.print(" [label=\"" + label + "\"]");
        }

        writer.println(";");
    }

    @Override
    public void transition(ModelState source, ModelTransition transition, ModelState target) {
        writer.println("\t " + source.getId() + " -> "+ target.getId() + ";");
    }

    @Override
    public void end() {
        writer.println("}");
        writer.flush();
    }

}
