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

package org.xid.explorer.dsl;

import java.util.Arrays;

/**
 * A DslState represents the state of a Dsl instance. It stores all the data needed to identify the state.
 */
public class DslState {

    private final char[] buffer;

    public DslState(int size) {
        buffer = new char[size];
    }

    public DslState(char[] buffer) {
        this.buffer = buffer;
    }

    // ///////////////////////////////////////////////////
    // Low level access methods
    // ///////////////////////////////////////////////////

    public int getInt(int index) {
        return buffer[index] << 16 | (int) buffer[index + 1];
    }

    public void setInt(int index, int value) {
         buffer[index] = (char) (value >> 16);
         buffer[index+1] = (char) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DslState dslState = (DslState) o;

        if (!Arrays.equals(buffer, dslState.buffer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return buffer != null ? Arrays.hashCode(buffer) : 0;
    }

    public DslState copy() {
        return new DslState(buffer == null ? null : Arrays.copyOf(buffer, buffer.length));
    }

}
