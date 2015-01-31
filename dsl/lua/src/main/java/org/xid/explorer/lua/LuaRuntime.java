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

package org.xid.explorer.lua;

import org.xid.explorer.ResourceResolver;
import org.xid.explorer.StreamUtil;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.dsl.DslRuntime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by j5r on 22/12/2014.
 */
public class LuaRuntime implements DslRuntime {

    @Override
    public String getId() {
        return "explorer.lua";
    }

    @Override
    public DslInstance createInstance(DslInstanceDescription description, ResourceResolver resourceResolver) throws Exception {
        StringBuilder script = new StringBuilder();
        for (String resource : description.getResources()) {
            InputStream stream = resourceResolver.readEntry(resource);
            if (stream == null) throw new IOException("Can't find resource '"+ resource +"'");
            script.append(StreamUtil.collectStream(stream, Charset.forName(StandardCharsets.UTF_8.name())));
            script.append("\n");
        }
        return new LuaInstance(description.getName(), description.getSize(), script.toString());
    }
}
