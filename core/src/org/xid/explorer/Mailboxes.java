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

/**
 *
 */
public class Mailboxes {
    /** Arrays of array of string to handle mailboxes. */
    private final String[][] mailboxes;

    public Mailboxes(String[][] mailboxes) {
        this.mailboxes = mailboxes;
    }

    public String[] getMailbox(int index) {
        return mailboxes[index];
    }

    public String removeFirst(int index) {
        String[] mailbox = mailboxes[index];
        String message = null;
        if (mailbox != null && mailbox.length > 0) {
            message = mailbox[0];
            mailbox = Arrays.copyOfRange(mailbox, 1, mailbox.length);
        }
        mailboxes[index] = mailbox == null || mailbox.length == 0 ? null : mailbox;
        return message;
    }

    public void addLast(int index, String message) {
        String[] mailbox = mailboxes[index];
        if (mailbox == null) {
            mailbox = new String[] { message};
        } else {
            mailbox = Arrays.copyOf(mailbox, mailbox.length+1);
            mailbox[mailbox.length-1] = message;
        }
        mailboxes[index] = mailbox;
    }


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
        if (mailboxes == null && other.mailboxes != null) return false;
        if (mailboxes != null) {
            for (int i = 0; i < mailboxes.length; i++) {
                if (!Arrays.equals(mailboxes[i], other.mailboxes[i])) return false;
            }
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
                text.append(Arrays.toString(mailbox));
                text.append("\n");
            }
        }
        return text.toString();
    }
}
