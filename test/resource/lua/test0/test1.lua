--
-- Simple counter from 0 to 2
--

local count = state:getInt(0);
if count < 2 then
    state:setInt(0, count +1);
else
    state:setInt(0, 0);
end
