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

package org.xid.explorer;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Mailboxes handles as set of String based messages mailboxes (event queues). It exposes a set of high-level methods
 * to create/test/destroy mailboxes and to add/removes messages from mailboxes.
 */
public class Mailboxes {

    /** Arrays of array of string to handle mailboxes. */
    private String[][] mailboxes;

    public Mailboxes() {
        this(null);
    }

    public Mailboxes(String[][] mailboxes) {
        this.mailboxes = mailboxes;
    }

    /**
     * Retrieves mailbox at given index. It return null if the mailbox doesn't exist.
     *
     * @param index mailbox index.
     * @return null if mailbox doesn't exist, null if the mailbox is empty, other wise the mailbox.
     */
    public String[] getMailbox(int index) {
        if (mailboxes == null || index < 0 || index >= mailboxes.length) return null;
        return mailboxes[index];
    }

    /**
     * @return Returns the mailbox count.
     */
    public int getMailboxesCount() {
        return mailboxes == null ? 0 : mailboxes.length;
    }

    /**
     * Creates a new mailbox at the end of mailboxes array.
     * @return the index of the newly created mailbox.
     */
    public int createMailbox() {
        if (mailboxes == null) {
            mailboxes = new String[1][];
        } else {
            mailboxes = Arrays.copyOf(mailboxes, mailboxes.length+1);
        }
        return mailboxes.length - 1;
    }

    /**
     * Destroys mailbox at given index.
     *
     * @param index mailbox index.
     * @return true if the mailbox was destroyed.
     */
    public boolean destroyMailbox(int index) {
        if (mailboxes == null || index < 0 || index >= mailboxes.length) return false;

        if (mailboxes.length == 1) {
            // only one mail, reset the mailbox array.
            mailboxes = null;
        } else {
            // swap mailboxes to push the one to remove at the end.
            for (int i = index; i < mailboxes.length-1; i++) {
                mailboxes[i] = mailboxes[i+1];
            }
            // truncates the mailboxes removing the last one
            mailboxes = Arrays.copyOf(mailboxes, mailboxes.length-1);
        }

        return true;
    }

    /**
     * @return Returns true is no message in any mailbox. It's also returns true if all mailboxes are empty.
     */
    public boolean isEmpty() {
        if (mailboxes == null) return true;
        for (String[] mailbox : mailboxes) {
            if (mailbox != null && mailbox.length > 0) return false;
        }
        return true;
    }

    /**
     * Returns true if the mailbox at given index is empty.
     * @param index mailbox index.
     * @return true if mailbox doesn't exist or is empty.
     */
    public boolean isMailboxEmpty(int index) {
        String[] mailbox = getMailbox(index);
        return mailbox == null || mailbox.length == 0;
    }

    /**
     * Tests a mailbox with given predicate. The test always returns false if the mailbox at given index doesn't exist
     * (out of bounds) or it's empty.
     *
     * @param index mailbox index.
     * @param predicate predicate to test the mailbox with. When called, the mailbox is never null. If the predicate is
     *                  null it returns true.
     * @return false if the mailbox doesn't exist or is empty or the result of the predicate.
     */
    public boolean testMailbox(int index, Predicate<String[]> predicate) {
        if (mailboxes == null || index < 0 || index >= mailboxes.length) return false;
        String[] mailbox = mailboxes[index];
        if (mailbox == null) return false;
        return predicate == null ? true : predicate.test(mailbox);
    }

    /**
     * Removes first message of a mailbox at given index if the predicate returns true. The test always return false if
     * the mailbox at given index doesn't exist (out of bounds) or is empty. There is a defensive code to avoid the test
     * of null message.
     *
     * @param index mailbox index.
     * @param predicate the predicate to test the first message with. If the predicate is null it's considered true.
     * @return null if the mailbox doesn't exist or is empty or the result of the predicate.
     */
    public String removeFirstIf(int index, Predicate<String> predicate) {
        if (mailboxes == null || index < 0 || index >= mailboxes.length) return null;
        String[] mailbox = mailboxes[index];
        if (mailbox != null && mailbox.length > 0) {
            String message = mailbox[0];
            if (message != null && (predicate == null || predicate.test(message))) {
                mailbox = Arrays.copyOfRange(mailbox, 1, mailbox.length);
                mailboxes[index] = mailbox == null || mailbox.length == 0 ? null : mailbox;
                return message;
            }
        }
        return null;
    }

    /**
     * Adds a message at the end of the mailbox at given index. if the mailbox doesn't exist or if the message is null
     * it returns false.
     *
     * @param index mailbox index.
     * @param message message to add. If null, nothing is done.
     * @return true if the message was added.
     */
    public boolean addLast(int index, String message) {
        if (mailboxes == null || index < 0 || index >= mailboxes.length) return false;
        if (message == null) return false;

        String[] mailbox = mailboxes[index];
        if (mailbox == null) {
            mailbox = new String[] { message};
        } else {
            mailbox = Arrays.copyOf(mailbox, mailbox.length+1);
            mailbox[mailbox.length-1] = message;
        }
        mailboxes[index] = mailbox;
        return true;
    }


    /**
     * Copy the mailboxes to a new instance.
     * @return a new instance as a copy of this.
     */
    public Mailboxes copy() {
        String[][] mailboxesCopy = null;
        if (mailboxes != null) {
            mailboxesCopy = Arrays.copyOf(mailboxes, mailboxes.length);
            for (int j = 0; j < mailboxesCopy.length; j++) {
                String[] messages = mailboxesCopy[j];
                if (messages != null) {
                    mailboxesCopy[j] = Arrays.copyOf(messages, messages.length);
                }
            }
        }
        return new Mailboxes(mailboxesCopy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mailboxes other = (Mailboxes) o;
        if (mailboxes != null && other.mailboxes != null && mailboxes.length == other.mailboxes.length) {
            for (int i = 0; i < mailboxes.length; i++) {
                if (!Arrays.equals(mailboxes[i], other.mailboxes[i])) return false;
            }
        } else {
            return mailboxes == null && other.mailboxes == null;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (mailboxes != null) {
            for (int i = 0; i < mailboxes.length; i++) {
                result = 31 * result + Arrays.hashCode(mailboxes[i]);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        if (mailboxes == null) {
            text.append("empty\n");
        } else {
            for (int i = 0; i < mailboxes.length; i++) {
                String[] mailbox = mailboxes[i];
                text.append("- (");
                text.append(i);
                text.append(") ");
                text.append(mailbox == null ? "empty" : Arrays.toString(mailbox));
                text.append("\n");
            }
        }
        return text.toString();
    }
}
