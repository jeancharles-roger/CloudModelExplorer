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

package org.xid.explorer.network;

import org.xid.explorer.ResourceResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * ResourceResolver for resources stored on an explorer server.
 */
public class ServerResourceResolver implements ResourceResolver {

    private final String resourceUrl;

    public ServerResourceResolver(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    @Override
    public InputStream readEntry(String path) throws IOException {
        URL url = new URL(resourceUrl + "/" + path);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    @Override
    public OutputStream writeEntry(String path) throws IOException {
        // TODO add post for entry
        throw new UnsupportedOperationException("Not yet");
    }
}
