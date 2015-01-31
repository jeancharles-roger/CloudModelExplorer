/*
 * Copyright 2015 to CloudModelExplorer
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

/**
 * Created by j5r on 19/11/2014.
 */
public final class ModelTransition {

    private final int sourceId;

    private final String[] actions;

    private final int targetId;

    public ModelTransition(int sourceId, String[] actions, int targetId) {
        this.sourceId = sourceId;
        this.actions = actions;
        this.targetId = targetId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public String[] getActions() {
        return actions;
    }

    public int getTargetId() {
        return targetId;
    }

}
