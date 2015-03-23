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

import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.result.ModelExploration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * Created by j5r on 01/03/2015.
 */
public class FileModelDataBase implements ModelDataBase {

    private final static String USERS_FOLDER = "users";
    private final static String PROFILE_FILE = "profile.json";

    private final static String MODELS_FOLDER = "models";
    private final static String MODEL_FILE = "model.json";

    private Path root;

    public FileModelDataBase(Path root) {
        this.root = root;
    }

    /**
     * Returns user folder path for given login.
     */
    private Path getUserPath(String login) {
        return root.resolve(USERS_FOLDER).resolve(login);
    }

    private Path getModelsPath(String login) {
        return getUserPath(login).resolve(MODELS_FOLDER);
    }

    private Path getModelPath(String login, String modelId) {
        return getModelsPath(login).resolve(modelId);
    }

    private Path getModelResourcePath(String login, String modelId, String resourceId) {
        return getModelPath(login,modelId).resolve(resourceId);
    }


    private boolean isModel(Path modeFilePath) {
        return Files.isRegularFile(modeFilePath.resolve(MODEL_FILE));
    }

    private Stream<Path> getAllModelPath(String login) {
        try {
            return Files.list(getModelsPath(login));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private ModelDescription loadModel(Path modelFilePath) {
        try (InputStream stream = Files.newInputStream(modelFilePath.resolve(MODEL_FILE))) {
            return ModelDescription.loadDescription(stream);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ReadableByteChannel getReadUserChannel(String userId) {
        return getReadChannel(getUserPath(userId).resolve(PROFILE_FILE));
    }

    @Override
    public WritableByteChannel getWriteUserChannel(String userId, boolean create) {
        return getWriteChannel(getUserPath(userId).resolve(PROFILE_FILE), create);
    }

    @Override
    public Stream<String> getAllModelIds(String userId) {
        Path modelsPath = getModelsPath(userId);
        try {
            return Files.list(modelsPath).filter(this::isModel).map((path) -> path.getFileName().toString());
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    @Override
    public ByteChannel getReadModelChannel(String userId, String modelId) {
        return getReadChannel(getModelPath(userId, modelId).resolve(MODEL_FILE));
    }

    @Override
    public ByteChannel getWriteModelChannel(String userId, String modelId, boolean create) {
        Path modelPath = getModelPath(userId, modelId).resolve(MODEL_FILE);
        return getWriteChannel(modelPath, create);
    }

    @Override
    public ByteChannel getReadModelResourceChannel(String userId, String modelId, String resourceId) {
        return getReadChannel(getModelResourcePath(userId, modelId, resourceId));
    }

    @Override
    public ByteChannel getWriteModelResourceChannel(String userId, String modelId, String resourceId, boolean create) {
        return getWriteChannel(getModelResourcePath(userId, modelId, resourceId), create);
    }

    @Override
    public Stream<String> getAllExplorationIds(String userId, String modelId) {
        return null;
    }

    @Override
    public ModelExploration getExploration(String userId, String modelId, String explorationId) {
        return null;
    }

    private ByteChannel getWriteChannel(Path path, boolean create) {
        try {
            if (create && Files.exists(path) == false) {
                Files.createDirectories(path.getParent());
            }
            return Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            // TODO add log
            return null;
        }
    }

    private ByteChannel getReadChannel(Path path) {
        try {
            return Files.newByteChannel(path);
        } catch (IOException e) {
            // TODO add log
            return null;
        }
    }
}
