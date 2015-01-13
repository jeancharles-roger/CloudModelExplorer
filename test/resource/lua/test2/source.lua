--
-- Starts a target instance and waits to stop
--

local mailbox = context:getModelDescription():getMailboxId("mailbox")
local current = state:getInt(0)

if current == 0 then
    mailboxes:addLast(mailbox, "start");
    state:setInt(0, 1);
else
    if mailboxes:removeFirstIfEquals(mailbox, "end") ~= nil then
        mailboxes:addLast(mailbox, "start");
    end
end
