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

import org.xid.explorer.Mailboxes;
import org.xid.explorer.dsl.DslState;

import java.util.Arrays;

/**
 * Represents a State for an exploration.
 */
public final class ModelState {

    private int id = -1;

    /** Arrays of states */
    private final DslState[] states;

    private final Mailboxes mailboxes;

    public ModelState(DslState[] states, Mailboxes mailboxes) {
        this.states = states;
        this.mailboxes = mailboxes == null ? new Mailboxes(null) : mailboxes;
    }

    public ModelState copy(int index, DslState modifiedState, Mailboxes  mailboxes) {
        DslState[] statesCopy = Arrays.copyOf(states, states.length);
        statesCopy[index] = modifiedState;
        return new ModelState(statesCopy, mailboxes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DslState getState(int index) {
        return states[index];
    }

    public DslState[] getStates() {
        return states;
    }

    public Mailboxes getMailboxes() {
        return mailboxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelState that = (ModelState) o;

        if (!mailboxes.equals(that.mailboxes)) return false;
        if (!Arrays.equals(states, that.states)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = states != null ? Arrays.hashCode(states) : 0;
        result = 31 * result + mailboxes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(id);
        text.append("\n");
        text.append("[Instances]\n");
        for (int i = 0; i < states.length; i++) {
            DslState state = states[i];
            text.append("- (");
            text.append(i);
            text.append(") ");
            text.append(state);
            text.append("\n");
        }
        text.append("[Mailboxes]\n");
        text.append(mailboxes);
        return text.toString();
    }

    public void printOn(ModelInstance instance, StringBuilder text) {
        text.append(id);
        text.append("\n");
        text.append("[Instances]\n");
        for (int i = 0; i < states.length; i++) {
            DslState state = states[i];
            text.append("- ");
            text.append(i);
            text.append(":");
           state.printOn(instance.getInstances()[i], text);
            text.append("\n");
        }
        text.append("[Mailboxes]\n");
        text.append(mailboxes);
    }
}
