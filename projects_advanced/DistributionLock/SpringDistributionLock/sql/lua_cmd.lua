-------------------------
-- Lua command example
-------------------------

-- https://youtu.be/YPR4xEUY_cI?si=6VitGwKpRqWvXSlp&t=415

-------------------------
-- Lua syntax (in redis)
-------------------------

-- 2 NECESSARY Param
--   1) EVAL
--   2) number of parameters
-- syntax : EVAL "op code" <number_of_key> <KEY>, <ARGV>
-- number_of_key : number of KEY, start from 1 (e.g. 1, 2, 3, ....)
-- KEY : split by space
-- ARGV : split by space

-------------------------
-- Lua cmd example (in redis)
-------------------------

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

-- 5) pass parameters (KEYS, ARGV)

-- use KEYS
-- 2 keys, KEYS[1] = 10, KEYS[2] = 20
EVAL "if KEYS[1]>KEYS[2] then return KEYS[1] else return KEYS[2] end" 2 10 20

-- use KEYS, ARGV
EVAL "if KEYS[1]>ARGV[1] then return KEYS[2] else return ARGV[2] end" 2 10 20 30 40

-- return multi values
EVAL "return {10, 20, 30}" 0

-- 4 : 4 keys, so 10 20 30 40 are keys
--     while 50 60 70 are ARGV
EVAL "return {KEYS[1], KEYS[2], ARGV[1], ARGV[2]}" 4 10 20 30 40 50 60 70

-- 127.0.0.1:6379> EVAL "return 'HELLO WORLD'" 0
-- "HELLO WORLD"
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "local a_=5 return a_" 0
-- (integer) 5
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "if KEYS[1]>KEYS[2] then return KEYS[1] else return KEYS[2] end" 2 10 20
-- "20"
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "if KEYS[1]>ARGV[1] then return KEYS[2] else return ARGV[2] end" 2 10 20 30 40
-- "40"
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "return {10, 20, 30}" 0
-- 1) (integer) 10
-- 2) (integer) 20
-- 3) (integer) 30
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "return {KEYS[1], KEYS[2], ARGV[1], ARGV[2]}" 4 10 20 30 40 50 60 70
-- 1) "10"
-- 2) "20"
-- 3) "50"
-- 4) "60"
