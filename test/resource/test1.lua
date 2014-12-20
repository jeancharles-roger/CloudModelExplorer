--
-- Simple counter from 0 to 10
--

local count = state:getInt(0);
if count < 10 then
    state:setInt(0, count +1);
end
