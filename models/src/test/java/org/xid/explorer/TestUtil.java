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

package org.xid.explorer;

import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.result.ModelExploration;
import org.xid.explorer.result.ModelResultDescription;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Set of utility methods for tests.
 */
public class TestUtil {

    public static void explore(String modelPath, int expectedStates, int expectedTransitions) throws Exception {
        ResourceResolver resourceResolver = new PathResourceResolver(Paths.get(modelPath));
        ModelDescription description = ModelDescription.loadDescription(resourceResolver.readEntry("model.json"));
        explore(description,resourceResolver, Arrays.asList(createDotResult(), createKryoResult()), expectedStates, expectedTransitions);
    }

    private static ModelResultDescription createDotResult() {
        ModelResultDescription dotResult = new ModelResultDescription("explorer.result.dot");
        dotResult.getParameters().put("detailed", Boolean.TRUE.toString());
        return dotResult;
    }

    private static ModelResultDescription createKryoResult() {
        return new ModelResultDescription("explorer.result.kryo");
    }

    public static void explore(ModelDescription model, ResourceResolver resourceResolver, List<ModelResultDescription> resultDescriptions, int expectedStates, int expectedTransitions) throws Exception {
        BFSExplorer explorer = new BFSExplorer(model, resourceResolver);
        explorer.initialize(resultDescriptions);
        ModelExploration modelExploration = explorer.explore(ActionMonitor.EMPTY);

        System.out.println(modelExploration);

        assertEquals(
                "Expected " + expectedStates + " states but explored " + modelExploration.getStateCount(),
                expectedStates, modelExploration.getStateCount()
        );

        assertEquals(
                "Expected " + expectedTransitions + " transitions but explored " + modelExploration.getTransitionCount(),
                expectedTransitions, modelExploration.getTransitionCount()
        );
    }
}
