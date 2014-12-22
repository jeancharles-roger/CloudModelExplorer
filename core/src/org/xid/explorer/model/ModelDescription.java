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

package org.xid.explorer.model;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.xid.explorer.dsl.DslInstanceDescription;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ModelDescription stores descriptive information about a model. It contains all needed information to create a
 * ModelInstance.
 */
public class ModelDescription {

    private String name;

    private List<DslInstanceDescription> instances;

    public ModelDescription() {
    }

    public ModelDescription(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DslInstanceDescription> getInstances() {
        return instances;
    }

    public void setInstances(List<DslInstanceDescription> instances) {
        this.instances = instances;
    }


    public static ModelDescription loadDescription(InputStream stream) throws IOException {
        try {
            JSONParser parser = new JSONParser(JSONParser.MODE_RFC4627);
            return parser.parse(stream, ModelDescription.class);
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        } finally {
            stream.close();
        }
    }
}



