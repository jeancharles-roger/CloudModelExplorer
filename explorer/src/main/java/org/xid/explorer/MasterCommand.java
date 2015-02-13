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

import io.airlift.airline.Command;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "master", description = "Starts a master explorer, it handles distributed exploration")
public class MasterCommand extends ClusterCommand {

    @Override
    public void run() {

    }

}
