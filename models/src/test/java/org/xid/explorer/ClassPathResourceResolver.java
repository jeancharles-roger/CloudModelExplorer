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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Resource resolver that searches in the classpath first for entry to read and write no where.
 *
 * TODO Adds an output folder
 */
public class ClassPathResourceResolver implements ResourceResolver {

    private final String baseName;

    public ClassPathResourceResolver(String path) {
        baseName = constructBasename(path);
    }

    private String constructBasename(String path) {
        StringBuilder result = new StringBuilder();

        String className = getClass().getName();
        int dot = className.indexOf('.');
        while (dot >= 0) {
            result.append("../");
            dot = className.indexOf('.', dot+1);
        }
        path = path.replaceAll("\\\\", "/");
        result.append(path);
        if (!path.endsWith("/")) result.append("/");
        return result.toString();
    }

    @Override
    public InputStream readEntry(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(baseName + path);
        if (stream == null) throw new IOException("Entry '"+ path +"' doesn't exist in classpath.");
        return stream;
    }

    @Override
    public OutputStream writeEntry(String path) throws IOException {
        return new ByteArrayOutputStream();
    }
}
