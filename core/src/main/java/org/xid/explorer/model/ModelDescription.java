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

import org.boon.Boon;
import org.xid.explorer.StreamUtil;
import org.xid.explorer.dsl.DslInstanceDescription;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ModelDescription stores descriptive information about a model. It contains all needed information to create a
 * ModelInstance.
 */
public class ModelDescription {

    private String name;

    private List<DslInstanceDescription> instances = new ArrayList<>();

    private List<MailboxDescription> mailboxes = new ArrayList<>();

    private transient Map<String, Integer> instanceToId = Collections.emptyMap();

    private transient Map<String, Integer> mailboxToId = Collections.emptyMap();

    public ModelDescription() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DslInstanceDescription> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    public void setInstances(List<DslInstanceDescription> instances) {
        this.instances = instances;
        initializeInstanceIds();
    }

    private void initializeInstanceIds() {
        // stores id for each instance name
        instanceToId = new HashMap<>(instances != null ? instances.size() : 0);
        if (instances != null) {
            for (int i = 0; i < instances.size(); i++) {
                instanceToId.put(instances.get(i).getName(), i);
            }
        }
    }

    public int getInstanceId(String name) {
        Integer id = instanceToId.get(name);
        return id != null ? id : -1;
    }

    public final DslInstanceDescription getInstanceDescription(int id) {
        // TODO handle newly created instances
        if (id < 0 || id > instances.size()) return null;
        return instances.get(id);
    }

    public List<MailboxDescription> getMailboxes() {
        return Collections.unmodifiableList(mailboxes);
    }

    public void setMailboxes(List<MailboxDescription> mailboxes) {
        this.mailboxes = mailboxes;
        initializeMailboxesId();
    }

    private void initializeMailboxesId() {
        // stores id for each instance name
        mailboxToId = new HashMap<>(mailboxes != null ? mailboxes.size() : 0);
        if (mailboxes != null) {
            for (int i = 0; i < mailboxes.size(); i++) {
                mailboxToId.put(mailboxes.get(i).getName(), i);
            }
        }
    }

    public int getMailboxId(String name) {
        Integer id = mailboxToId.get(name);
        return id != null ? id : -1;
    }

    public final MailboxDescription getMailboxDescription(int id) {
        // TODO handle newly created mailboxes
        if (id < 0 || id > mailboxes.size()) return null;
        return mailboxes.get(id);
    }

    public final int getIntParameterValue(String instance, String name, int defaultValue) {
        return getParameterValue(instance, name, Number.class, defaultValue).intValue();
    }

    public final <T> T getParameterValue(String instance, String name, Class<T> type, T defaultValue) {
        if (instance == null || name == null) return defaultValue;
        int id = getInstanceId(instance);
        if (id < 0) return null;

        DslInstanceDescription description = getInstanceDescription(id);
        if (description == null) return null;

        Object value = description.getInstanceParameter(name);
        if (value == null) return defaultValue;
        if (type.isInstance(value) == false) return defaultValue;
        return type.cast(value);
    }

    @Override
    public String toString() {
        return Boon.toJson(this);
    }

    public static ModelDescription loadDescription(InputStream stream) throws IOException {
        String content = StreamUtil.collectStream(stream, StandardCharsets.UTF_8);
        ModelDescription description = Boon.fromJson(content, ModelDescription.class);
        description.initializeInstanceIds();
        description.initializeMailboxesId();
        return description;
    }

}



