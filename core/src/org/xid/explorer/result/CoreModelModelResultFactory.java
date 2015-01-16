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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by j5r on 14/01/2015.
 */
public class CoreModelModelResultFactory implements ModelResultFactory {

    private final static Set<String> KNOWN_TYPES = new HashSet<>(Arrays.asList(
            "explorer.result.dot",
            "explorer.result.kryo"
    ));

    @Override
    public Set<String> getKnownTypes() {
        return KNOWN_TYPES;
    }

    @Override
    public ModelResultHandler createResult(ExplorationContext context, ModelResultDescription description) throws IOException {
        switch (description.getType()) {
            case "explorer.result.dot":
                return createDotHandler(context, description);

            case "explorer.result.kryo":
                return createKryoHandler(context, description);

            default:
                return null;
        }
    }

    private ModelResultHandler createDotHandler(ExplorationContext context, ModelResultDescription description) throws IOException {
        String path = description.getPath() != null ? description.getPath() : "exploration.dot";
        PrintWriter writer = new PrintWriter(context.getResourceResolver().writeEntry(path));
        boolean detailed = Boolean.parseBoolean(description.getParameters().get("detailed"));
        return new ModelResultDotPrinter(context.getModelInstance(), writer, detailed);
    }

    private ModelResultHandler createKryoHandler(ExplorationContext context, ModelResultDescription description) throws IOException {
        String path = description.getPath() != null ? description.getPath() : "exploration.kryo";
        OutputStream out = context.getResourceResolver().writeEntry(path);
        return new ModelResultBinaryWriter(context.getModelInstance(), out);
    }
}
