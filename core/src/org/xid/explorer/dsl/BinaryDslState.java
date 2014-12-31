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

package org.xid.explorer.dsl;

import java.util.Arrays;

/**
 * Created by j5r on 24/12/2014.
 */
public final class BinaryDslState implements DslState {

    private byte[] buffer;

    public BinaryDslState(int size) {
        buffer = new byte[size];
        Arrays.fill(buffer, Byte.MIN_VALUE);
    }

    public BinaryDslState(byte[] buffer) {
        this.buffer = buffer;
    }

    // ///////////////////////////////////////////////////
    // Low level access methods
    // ///////////////////////////////////////////////////


    @Override
    public byte[] getBytes() {
        return buffer;
    }

    @Override
    public void setBytes(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public int getInt(int index) {
        int one   = (buffer[index  ] - Byte.MIN_VALUE) << 24;
        int two   = (buffer[index+1] - Byte.MIN_VALUE) << 16;
        int three = (buffer[index+2] - Byte.MIN_VALUE) << 8;
        int four  =  buffer[index+3] - Byte.MIN_VALUE;
        return one | two | three | four;
    }

    public void setInt(int index, int value) {
        buffer[index]  =  (byte) ((value>>24) + Byte.MIN_VALUE);
        buffer[index+1] = (byte) ((value>>16) + Byte.MIN_VALUE);
        buffer[index+2] = (byte) ((value>> 8) + Byte.MIN_VALUE);
        buffer[index+3] = (byte) ( value      + Byte.MIN_VALUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryDslState dslState = (BinaryDslState) o;

        if (!Arrays.equals(buffer, dslState.buffer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return buffer != null ? Arrays.hashCode(buffer) : 0;
    }

    @Override
    public DslState copy() {
        return new BinaryDslState(buffer == null ? null : Arrays.copyOf(buffer, buffer.length));
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("State(");
        text.append(buffer.length);
        text.append(")[");
        /*
        for (int i = 0; i < buffer.length; i++) {
            text.append(Integer.toHexString((int) buffer[i]));
        }
        */
        for (int i = 0; i < buffer.length / 4; i++) {
            if (i>0) text.append(",");
            text.append(getInt(i));
        }
        text.append("]");
        return text.toString();
    }
}
