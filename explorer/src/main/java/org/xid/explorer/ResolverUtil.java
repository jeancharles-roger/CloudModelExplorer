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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ResolverUtil contains utility functions for ResourceResolver
 */
public class ResolverUtil {

    /**
     * This method creates a ResourceResolver from the given path. If path is a directory, the returned ResourceResolver
     * reads and writes its files in the directory. If path is a file, it tries to open it as a zip file to read files.
     * The files to write are put in the given file directory.
     *
     * @param modelPath path from which to create the ResourceResolver.
     * @return a ResourceResolve.
     * @throws IOException if path is a file which can't be read as a zip or if path isn't a directory nor a regular file
     */
    public static ResourceResolver createResourceResolver(Path modelPath) throws IOException {
        if (Files.isDirectory(modelPath)) {
            return new PathResourceResolver(modelPath);
        }
        if (Files.isRegularFile(modelPath)) {
            return new ZipResourceResolver(modelPath, modelPath.getParent());
        }
        throw new IOException("Path '"+ modelPath +"' isn't readable");
    }
}
