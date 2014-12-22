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

package org.xid.explorer.dsl;

import org.xid.explorer.ResourceResolver;

/**
 * A DslRuntime implements the evaluation for a given Dsl. It's able to evaluate transition from a source to an other
 * and checks a expression value.
 */
public interface DslRuntime {

    /** Returns the Dsl id, must be unique. */
    String getId();

    /**
     * Creates a new DslInstance from given DslInstanceDescription.
     *
     * @param description instance description
     * @param resourceResolver function that take a String and returns an open InputStream (or null).
     * @return a newly created DslInstance.
     * @throws Exception if the description isn't sound or some resources aren't present.
     */
    DslInstance createInstance(DslInstanceDescription description, ResourceResolver resourceResolver) throws Exception;

}
