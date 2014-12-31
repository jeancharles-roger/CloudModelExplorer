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

/**
 * A DslState represents the state of a Dsl instance. It stores all the data needed to identify the state.
 */
public interface DslState {

    // ///////////////////////////////////////////////////
    // Low level access methods
    // ///////////////////////////////////////////////////

    byte[] getBytes();
    void setBytes(byte[] buffer);

    int getInt(int index);

    void setInt(int index, int value);

    boolean equals(Object o);

    int hashCode();

    DslState copy();

}
