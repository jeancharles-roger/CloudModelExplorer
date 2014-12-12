--
-- Simple counter from 0 to 10
--

local count = state.getInt(current, 0);
if count < 10 then
    state.setInt(current, 0, count + 1);
    return true;
else
    return false;
end
