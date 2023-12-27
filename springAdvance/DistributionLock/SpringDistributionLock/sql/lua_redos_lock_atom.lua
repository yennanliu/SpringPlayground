----------------------------------------
-- Lua cmd for ATOM Redis lock
----------------------------------------

-- https://youtu.be/ZLNbNbfVXEw?si=b_NDMKpCvmVtzHxS

----------------------------------------
-- redis.call syntax
----------------------------------------
-- redis.call syntax :  same as cmd in redis-cli (same param ordering)
-- example :
--  redis-cli : set key 777-777-7777
--  lua :       EVAL "redis.call('set', 'key', '777-777-7777')" 0

----------------------------------------
-- prepare
----------------------------------------
set lock 534-534564-645654

get lock

EVAL "return redis.call('get', 'lock')" 0

EVAL "return redis.call('set', lock2', '99-756-888')" 0

-- with param
EVAL "return redis.call('set', KEYS[1], ARGV[1])" 1 lock3 333-33-333

EVAL "return redis.call('get', 'lock3')" 0


-- 127.0.0.1:6379> EVAL "return redis.call('get', 'lock')" 0
-- (nil)
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> set lock 534-534564-645654
-- OK
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> get lock
-- "534-534564-645654"
-- 127.0.0.1:6379> EVAL "return redis.call('get', 'lock')" 0
-- "534-534564-645654"
-- 127.0.0.1:6379>
-- 127.0.0.1:6379> EVAL "return redis.call('set', KEYS[1], ARGV[1])" 1 lock3 333-33-333
--
-- OK
-- 127.0.0.1:6379> EVAL "return redis.call('get', 'lock3')" 0
-- "333-33-333"

----------------------------------------
-- Actual Lua script
----------------------------------------

-- logic : 1) check if itself lock, if true, then delete lock (release)

-- Lua code

-- if redis.call('get', 'lock') == uuid
-- then
--     return redis.call('del', 'lock')
-- else
--     return 0
-- end

if redis.call('get', KEYS[1]) == ARGV[1]
then
    return redis.call('del', KEYS[1])
else
    return 0
end

key: lock
arg: uuid

-- in Redis form
set lock 777-888-888

EVAL "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end " 1 lock 777-888-888