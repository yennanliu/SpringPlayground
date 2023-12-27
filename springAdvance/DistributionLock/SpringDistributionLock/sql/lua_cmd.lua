-------------------------
-- Lua command example
-------------------------

-- https://youtu.be/YPR4xEUY_cI?si=6VitGwKpRqWvXSlp&t=415

-- 2 NECESSARY Param
--   1) EVAL
--   2) number of parameters
-- syntax : EVAL "op code" <key>, <args>

-- 1) execution result is NOT print result, but the cmd return value
EVAL "return 'HELLO WORLD'" 0

-- 2) variable : local, global

-- global var
EVAL "a=5 return a" 0  -- however, global var is NOT allowed in redis, so we use local var instead

-- local var
EVAL "local a_=5 return a_" 0

-- 3) for loop
EVAL "for var=1, 10, 1 do return (var) end" 0


-- 4) if else logic
EVAL "if 10>20 then return 10 else return 20 end" 0

-- pass parameters
EVAL "if 10>20 then return 10 else return 20 end" 2 10 20



-- 127.0.0.1:6379> EVAL "return 'HELLO WORLD'" 0
-- "HELLO WORLD"
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "local a_=5 return a_" 0
-- (integer) 5