--
-- Starts a target instance and waits to stop
--

if mailboxes:getMailboxesCount() == 0 then
    local index = mailboxes:createMailbox();
    mailboxes:addLast(index, "start");
    state:setInt(0, index);
else
    local index = state:getInt(0);
    if mailboxes:removeFirstIfEquals(index, "end") ~= nil then
        mailboxes:addLast(index, "start");
    end
end
