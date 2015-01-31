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

import org.junit.Test;
import org.xid.explorer.Mailboxes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MailboxesTest {

    @org.junit.Test
    public void testCreateMailbox() throws Exception {
        Mailboxes mailboxes = new Mailboxes();

        // creates 10 mailboxes.
        for (int i = 0; i < 10; i++) {
            int index = mailboxes.createMailbox();
            assertEquals(i, index);
            assertEquals(i + 1, mailboxes.getMailboxesCount());
        }
    }

    @org.junit.Test
    public void testDestroyMailbox() throws Exception {
        Mailboxes mailboxes = new Mailboxes();
        for (int i = 0; i < 10; i++) mailboxes.createMailbox();

        // removes the 5 first mailboxes
        for (int i = 0; i < 5; i++) {
            boolean result = mailboxes.destroyMailbox(i);
            assertEquals(true, result);
            assertEquals(5 + (4 - i), mailboxes.getMailboxesCount());
        }

        // removes the 5 remaining in inverted order
        for (int i = 4; i >= 0; i--) {
            boolean result = mailboxes.destroyMailbox(i);
            assertEquals(true, result);
            assertEquals(i, mailboxes.getMailboxesCount());
        }
    }

    @org.junit.Test
    public void testIsEmpty() throws Exception {
        Mailboxes mailboxes = new Mailboxes();
        assertEquals(true, mailboxes.isEmpty());

        for (int i = 0; i < 10; i++) mailboxes.createMailbox();
        assertEquals(true, mailboxes.isEmpty());

        // adds one message in each mailbox
        for (int i = 0; i < 10; i++) {
            boolean result = mailboxes.addLast(i, "Hello");
            assertEquals(true, result);
            assertEquals(false, mailboxes.isEmpty());
        }

        // removes the message from each mailbox
        for (int i = 0; i < 10; i++) {
            String message = mailboxes.removeFirst(i);
            assertEquals("Hello", message);
            assertEquals(i == 9, mailboxes.isEmpty());
        }
    }

    @org.junit.Test
    public void testIsMailboxEmpty() throws Exception {
        Mailboxes mailboxes = new Mailboxes();

        for (int i = 0; i < 10; i++) mailboxes.createMailbox();
        assertEquals(true, mailboxes.isEmpty());

        // adds one message in each mailbox
        for (int i = 0; i < 10; i++) {
            boolean result = mailboxes.addLast(i, "Hello");
            assertEquals(true, result);
            assertEquals(false, mailboxes.isMailboxEmpty(i));
        }

        // removes the message from each mailbox
        for (int i = 0; i < 10; i++) {
            String message = mailboxes.removeFirst(i);
            assertEquals("Hello", message);
            assertEquals(true, mailboxes.isMailboxEmpty(i));
        }
    }

    @org.junit.Test
    public void testTestMailbox() throws Exception {
        Mailboxes mailboxes = new Mailboxes();
        for (int i = 0; i < 10; i++) mailboxes.createMailbox();

        for (int i = 0; i < 10; i++) {
            final int index = i;
            boolean result = mailboxes.testMailbox(i, (mailbox) -> {
                fail("Predicate shouldn't be call, since mailbox is empty");
                return false;
            });
            assertEquals(false, result);

            result = mailboxes.addLast(i, "Hello" + i);
            assertEquals(true, result);

            result = mailboxes.testMailbox(i, (mailbox) -> ("Hello" + index).equals(mailbox[0]));
            assertEquals(true, result);
        }
    }

    @org.junit.Test
    public void testRemoveFirstIf() throws Exception {
        Mailboxes mailboxes = new Mailboxes();
        for (int i = 0; i < 10; i++) mailboxes.createMailbox();

        for (int i = 0; i < 10; i++) {
            String value = mailboxes.removeFirstIf(i, (message) -> {
                fail("Predicate shouldn't be call, since mailbox is empty");
                return false;
            });
            assertEquals(null, value);

            boolean result = mailboxes.addLast(i, "Hello1");
            assertEquals(true, result);

            result = mailboxes.addLast(i, "Hello2");
            assertEquals(true, result);

            value = mailboxes.removeFirstIf(i, (message) -> message.equals("Hello2"));
            assertEquals(null, value);

            value = mailboxes.removeFirstIf(i, (message) -> message.equals("Hello1"));
            assertEquals("Hello1", value);

            value = mailboxes.removeFirstIf(i, (message) -> message.equals("Hello2"));
            assertEquals("Hello2", value);
        }
    }

    @org.junit.Test
    public void testAddLast() throws Exception {
        Mailboxes mailboxes = new Mailboxes();
        for (int i = 0; i < 10; i++) mailboxes.createMailbox();

        for (int i = 0; i < 10; i++) {
            // adds 10 messages to the mailbox
            for (int j = 0; j < 10; j++) {
                boolean result = mailboxes.addLast(i, "Hello"+j);
                assertEquals(true, result);
            }

            // test mailbox to check order
            boolean result = mailboxes.testMailbox(i, (mailbox) -> {
                        assertEquals(10, mailbox.length);
                        for (int j = 0; j < mailbox.length; j++) {
                            assertEquals("Hello" + j, mailbox[j]);
                        }
                        return true;
                }
            );
            assertEquals(true, result);
        }
    }

    @Test
    public void testCopyPerformances() throws Exception {
        Mailboxes mailboxes = new Mailboxes();

        // creates 1000 mailbox with 1000 messages each
        for (int i = 0; i < 1000; i++) {
            mailboxes.createMailbox();
            for (int j = 0; j < 1000; j++) {
                mailboxes.addLast(i, "m" + j);
            }
        }

        for (int i = 0; i < 3; i++) {
            long start = System.currentTimeMillis();
            int n = 1_000;
            // copy n times these mailboxes
            for (int j = 0; j < n; j++) {
                Mailboxes copy = mailboxes.copy();
                assertEquals(false, copy.isEmpty());
            }

            long end = System.currentTimeMillis();
            System.out.println("Copied "+ n +" times in "+ (end-start) +"ms.");
        }
    }
}
