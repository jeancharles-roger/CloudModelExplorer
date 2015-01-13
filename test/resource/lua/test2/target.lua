-- Copyright 2015 to CloudModelExplorer authors
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

--
-- Count from 0 to 10 on a start trigger frm the first mailbox
--

local mailbox = context:getModelDescription():getMailboxId("mailbox")

local count = state:getInt(0);
if count == 0 then
    if mailboxes:removeFirstIfEquals(mailbox, "start") ~= nil then
        count = 1;
    end
elseif count >= 10 then
    count = 0;
    mailboxes:addLast(mailbox, "end");
else
    count = count + 1;
end
state:setInt(0, count);

