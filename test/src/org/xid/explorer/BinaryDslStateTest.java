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

package org.xid.explorer;

import org.junit.Test;
import org.xid.explorer.dsl.BinaryDslState;
import org.xid.explorer.dsl.DslState;

import static org.junit.Assert.*;

/**
 * Created by j5r on 19/11/2014.
 */
public class BinaryDslStateTest {

    @Test
    public void testInt() {
        DslState state = new BinaryDslState(8);
        state.setInt(0, 0x0000ffff);
        assertEquals(0x0000ffff, state.getInt(0));

        state.setInt(0, 4);
        assertEquals(4, state.getInt(0));

        state.setInt(4, -42);
        assertEquals(4, state.getInt(0));
        assertEquals(-42, state.getInt(4));

        state.setInt(4, Integer.MAX_VALUE);
        assertEquals(4, state.getInt(0));
        assertEquals(Integer.MAX_VALUE, state.getInt(4));
    }

    @Test
    public void testIntToMax() {
        DslState state = new BinaryDslState(4);
        int step = Integer.MAX_VALUE / 10000;
        for (long i = 0; i < Integer.MAX_VALUE; i+= step) {
            state.setInt(0, (int) i);
            assertEquals(i, state.getInt(0));
        }

    }


    @Test
    public void testEquals() {
        DslState state1 = new BinaryDslState(4);
        DslState state2 = new BinaryDslState(4);

        assertTrue(state1.equals(state1));
        assertTrue(state1.equals(state2));
        assertTrue(state2.equals(state1));
        assertEquals(state1.hashCode(), state2.hashCode());

        state1.setInt(0, 1234567890);
        assertFalse(state1.equals(state2));
        assertFalse(state2.equals(state1));
        assertNotEquals(state1.hashCode(), state2.hashCode());

        state2.setInt(0, 1234567890);
        assertTrue(state1.equals(state2));
        assertTrue(state2.equals(state1));
        assertEquals(state1.hashCode(), state2.hashCode());
    }


    @Test
    public void testPerformance() {
        DslState state = new BinaryDslState(8);
        for (int i=0; i<100_000_000; i++) {
            state.setInt(0, i);
            assertEquals(i, state.getInt(0));
            state.setInt(4, Integer.MAX_VALUE - i);
            assertEquals(Integer.MAX_VALUE - i, state.getInt(4));
        }
    }
}
