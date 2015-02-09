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
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import io.airlift.airline.Command;
import org.xid.explorer.message.SlaveMessage;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "master", description = "Starts a master explorer, it handles distributed exploration")
public class MasterCommand extends ClusterCommand {

    /** Thread safe list of running slaves. */
    private final List<String> runningSlaves = new Vector<>();

    @Override
    public void run() {
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(createConfig());


        ConcurrentMap<String, String> sharedProperties = instance.getMap("shared-properties");

        // declares master in the shared properties
        String host;
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
            if (sharedProperties.putIfAbsent("master", host) != null) {
                fatalError("Cluster '" + cluster + "' already as a master", 1, null);
            }
        } catch (UnknownHostException e) {
            fatalError("Can't determinate local host address", 1, e);
        }

        // listener on slave activity
        final IQueue<SlaveMessage> slaveStatusQueue = instance.getQueue("slave-status-queue");
        slaveStatusQueue.addItemListener(new ItemListener<SlaveMessage>() {
            @Override
            public void itemAdded(ItemEvent<SlaveMessage> item) {
                //waits for slaves to register
                String uuid = item.getMember().getUuid();
                switch (item.getItem().getStatus()) {
                case Started:
                    info("Slave '"+ uuid +"' started");
                    runningSlaves.add(uuid);
                    break;
                case Stopped:
                    info("Slave '" + uuid +"' stopped");
                    runningSlaves.remove(uuid);
                    break;
                }
                slaveStatusQueue.remove(item.getItem());
            }

            @Override
            public void itemRemoved(ItemEvent<SlaveMessage> item) {
                // nothing to do
            }
        }, true);



    }

}
