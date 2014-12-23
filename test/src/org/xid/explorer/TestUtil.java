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
import org.xid.explorer.model.ModelInstance;
import org.xid.explorer.result.ModelExploration;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Set of utility methods for tests.
 */
public class TestUtil {

    public static void explore(String modelPath, int expectedStates, int expectedTransitions) throws Exception {
        ResourceResolver resourceResolver = path -> Files.newInputStream(Paths.get(modelPath + path));
        ModelDescription description = ModelDescription.loadDescription(resourceResolver.resolve("model.json"));
        explore(ModelInstance.load(description, resourceResolver), expectedStates, expectedTransitions);
    }

    public static void explore(ModelInstance model, int expectedStates, int expectedTransitions) {
        BFSExplorer explorer = new BFSExplorer(model);
        ModelExploration modelExploration = explorer.explore();

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
