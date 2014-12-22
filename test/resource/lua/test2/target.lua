--
-- Count from 0 to 10 on a start trigger frm the first mailbox
--

local count = state:getInt(0);
if count == 0 then
    if mailboxes:getMailboxesCount() > 0 then
        local result = mailboxes:removeFirst(0);
        if result == "start" then count = 1; end
    end
elseif count >= 10 then
    count = 0;
    mailboxes:addLast(0, "end");
else
    count = count + 1;
end
state:setInt(0, count);

