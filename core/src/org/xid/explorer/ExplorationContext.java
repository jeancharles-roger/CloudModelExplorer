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

import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.model.ModelInstance;

/**
 * ExplorationContext stores information for a running exploration
 */
public interface ExplorationContext {

    ModelInstance getModelInstance();


    // TODO Handle instances description
    DslInstanceDescription getInstanceDescription(int id);

    // TODO Handle mailboxes names

}
