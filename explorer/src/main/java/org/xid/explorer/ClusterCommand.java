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

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.config.SerializerConfig;
import io.airlift.airline.Option;
import org.xid.explorer.message.KryoSerializer;
import org.xid.explorer.message.SlaveMessage;

public abstract class ClusterCommand extends ExplorerCommand {

    @Option(name = {"-c", "-cluster"}, description = "Cluster name to shared informations with agent")
    public String cluster = "explorer";

    public Config createConfig() {
        Config config = new Config();
        config.setInstanceName(cluster);

        SerializationConfig serializationConfig = config.getSerializationConfig();
        serializationConfig.addSerializerConfig(createSerializerConfig(SlaveMessage.class));
        return config;
    }

    private SerializerConfig createSerializerConfig(Class<SlaveMessage> typeClass) {
        SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.setTypeClass(typeClass);
        serializerConfig.setImplementation(new KryoSerializer<>(typeClass));
        return serializerConfig;
    }
}
