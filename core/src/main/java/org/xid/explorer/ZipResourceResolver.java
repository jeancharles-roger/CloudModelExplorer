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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A ZipResourceResolver reads entry from inside a zip given as parameter. It writes entries in directory given as
 * parameter.
 */
public class ZipResourceResolver implements ResourceResolver {

    private final Path zipPath;
    private final Path outputPath;

    private final ZipFile zipFile;

    public ZipResourceResolver(Path zipPath, Path outputPath) throws IOException {
        this.zipPath = zipPath;
        this.outputPath = outputPath;
        this.zipFile = new ZipFile(zipPath.toString());
    }


    @Override
    public InputStream readEntry(String path) throws IOException {
        ZipEntry entry = zipFile.getEntry(path);
        if (entry == null) throw new IOException("Entry '"+ path +"' doesn't exist in zip file '"+ zipPath +"'.");
        return zipFile.getInputStream(entry);
    }

    @Override
    public OutputStream writeEntry(String path) throws IOException {
        return Files.newOutputStream(outputPath.resolve(path));
    }
}
