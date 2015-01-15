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

import org.xid.explorer.ExplorationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by j5r on 14/01/2015.
 */
public class CoreModelModelResultFactory implements ModelResultFactory {

    private final static Set<String> KNOWN_TYPES = new HashSet<>(Arrays.asList(
            "explorer.result.dot"
    ));

    @Override
    public Set<String> getKnownTypes() {
        return KNOWN_TYPES;
    }

    @Override
    public ModelExplorationHandler createResult(ExplorationContext context, ModelResultDescription description) throws IOException {
        switch (description.getType()) {
            case "explorer.result.dot":
                PrintWriter writer = new PrintWriter(context.getResourceResolver().writeEntry("exploration.dot"));
                boolean detailed = Boolean.parseBoolean(description.getParameters().get("detailed"));
                return new ModelExplorationDotPrinter(context.getModelInstance(), writer, detailed);

            default:
                return null;
        }
    }
}
