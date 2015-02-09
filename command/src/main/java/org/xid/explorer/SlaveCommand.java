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

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import io.airlift.airline.Command;
import org.xid.explorer.message.SlaveMessage;
import org.xid.explorer.message.SlaveMessage.Status;

import java.util.concurrent.ConcurrentMap;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "slave", description = "Starts a agent explorer, it explores models ordered by a master")
public class SlaveCommand extends ClusterCommand {

    @Override
    public void run() {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(createConfig());

        ConcurrentMap<String, String> sharedProperties = instance.getMap("shared-properties");

        // checks if master is present
        if (sharedProperties.get("master") == null) {
            fatalError("Cluster '"+ cluster +"' has no master", 1, null);
        }

        final IQueue<SlaveMessage> slaveStatusQueue = instance.getQueue("slave-status-queue");
        slaveStatusQueue.add(new SlaveMessage(Status.Started));

        try { Thread.sleep(3000); } catch (InterruptedException e) {}


        slaveStatusQueue.add(new SlaveMessage(Status.Stopped));
    }

}
