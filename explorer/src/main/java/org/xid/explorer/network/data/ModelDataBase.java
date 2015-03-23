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

package org.xid.explorer.network.data;

import org.xid.explorer.result.ModelExploration;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.stream.Stream;

/**
 * ModelDataBase offers access to models and explorations organized by users.
 */
public interface ModelDataBase {

    ReadableByteChannel getReadUserChannel(String userId);
    WritableByteChannel getWriteUserChannel(String userId, boolean create);

    Stream<String> getAllModelIds(String userId);

    ReadableByteChannel getReadModelChannel(String userId, String modelId);
    WritableByteChannel getWriteModelChannel(String userId, String modelId, boolean create);

    ReadableByteChannel getReadModelResourceChannel(String userId, String modelId, String resourceId);
    WritableByteChannel getWriteModelResourceChannel(String userId, String modelId, String resourceId, boolean create);

    Stream<String> getAllExplorationIds(String userId, String modelId);

    ModelExploration getExploration(String userId, String modelId, String explorationId);
}
