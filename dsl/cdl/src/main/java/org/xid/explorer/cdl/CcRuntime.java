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

package org.xid.explorer.cdl;

import obp.cc.ConcreteContext;
import obp.cdl.CDLDeclaration;
import obp.cdl.CDLUnit;
import obp.transfo.cdl.CDLToConcreteContext;
import obp.util.CDLUtil;
import org.xid.explorer.ResourceResolver;
import org.xid.explorer.StreamUtil;
import org.xid.explorer.dsl.DslInstance;
import org.xid.explorer.dsl.DslInstanceDescription;
import org.xid.explorer.dsl.DslRuntime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by j5r on 13/01/2015.
 */
public class CcRuntime implements DslRuntime {
    @Override
    public String getId() {
        return "explorer.cdl";
    }

    @Override
    public DslInstance createInstance(DslInstanceDescription description, ResourceResolver resourceResolver) throws Exception {
        final Map<String, String> dslParameters = description.getDslParameters();
        String cdlName = dslParameters.get("cdl");
        if (cdlName == null) throw new Exception("No cdl parameter provided.");

        StringBuilder cdl = new StringBuilder();
        for (String resource : description.getResources()) {
            InputStream stream = resourceResolver.readEntry(resource);
            if (stream == null) throw new IOException("Can't find resource '"+ resource +"'");
            cdl.append(StreamUtil.collectStream(stream, Charset.forName(StandardCharsets.UTF_8.name())));
            cdl.append("\n");
        }

        CDLUnit cdlUnit = CDLUtil.readCDLAndResolveReferences(cdl.toString());
        CDLDeclaration cdlDeclaration = CDLUtil.findDeclaration(cdlUnit, cdlName, CDLDeclaration.class);
        ConcreteContext concreteContext = CDLToConcreteContext.toConcreteContext(cdlUnit, cdlDeclaration);
        return new CcInstance(concreteContext);
    }
}
