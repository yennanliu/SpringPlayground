---------------------------------------------------------
-- Redis ReentrantLock lock implementation with Lua script
---------------------------------------------------------

-- https://youtu.be/V-OREDbG2UA?si=dNHrBr83Gq5dR8RY

-------------------------------------
-- Syntax
-------------------------------------

-- Syntax
-- 1. hset <lock_name> <key> <value>

-------------------------------------
-- Lock
-------------------------------------

-- Lock Steps
--  1. check whether lock existed (exists), if not, get lock directly (hset key field value 1)
--  2. if lock existed  (state!=0), check if such lock is owned by its thread (HEXISTS lock <key>) (the thread which create the lock)
--     if true, then 重入 (hincrby lock <key> 1)
--     if false, retry (recursion or iteration call)

hset lock 66-666-666 1
HEXISTS lock 66-666-666
hincrby lock 66-666-666 1

--127.0.0.1:6379> hset lock 66-666-666 1
--(integer) 1
--127.0.0.1:6379> HEXISTS lock 66-666-666
--(integer) 1
--127.0.0.1:6379> HEXISTS lock 11-111
--(integer) 0
--127.0.0.1:6379> hincrby lock 66-666-666 1
--(integer) 2

-------------------------------------
-- Lock Redis Lua cmd
-------------------------------------

--------------------
-- ### V1
--------------------

-- if lock not existed
if redis.call('exists', 'lock') == 0
then
--  lock directly, return true
    redis.call('hset', 'lock', uuid, 1)
    redis.call('expire', 'lock', 30)
    return 1
-- if lock existed, check if lock owned by current thread
elseif redis.call('hexists', 'lock', uuid) == 1
then
--  if above is true, add increase val by 1, get lock, return true
    redis.call('hincrby', 'lock', uuid, 1)
    redis.call('expire', 'lock', 30)
    return 1
-- if above is false, lock failed, return false
else
    return 0
end

--------------------
-- ### V2
--------------------

-- if lock not existed or hexists == 1
if redis.call('exists', 'lock') == 0 or redis.call('hexists', 'lock', uuid) == 1
then
--  lock directly, return true
    redis.call('hincrby', 'lock', uuid, 1)
    redis.call('expire', 'lock', 30)
    return 1
else
    return 0
end

--------------------
-- ### V2 with param
--------------------

if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1
then
--  lock directly, return true
    redis.call('hincrby', KEYS[1], ARGV[1], 1)
    redis.call('expire', KEYS[1], ARGV[2])
    return 1
else
    return 0
end

key: lock
arg: uuid expire_time


--------------------
-- ### V2 with param in redis cmd form
--------------------

if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then redis.call('hincrby', KEYS[1], ARGV[1], 1) redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end


EVAL "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then redis.call('hincrby', KEYS[1], ARGV[1], 1) redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end" 1 lock 333-333-33 30

-------------------------------------
-- Unlock
-------------------------------------

-- https://youtu.be/X4qAl1-yC3s?si=Fj7QC4Ri6ONKI3Lh&t=13

-- Unlock Steps
-- 1. check if lock existed
-- 2. check if lock is owned by current thread (HEXISTS)
--    if false, return nil
--    if true, count -= 1  (HINCRBY, -1)
--             if updated count == 0, release lock (del lock), and return true,
--             if updated count != 0, return false

if redis.call('EXISTS', lock) and redis.call('HEXISTS', lock, uuid)
then
    return 1
end



--127.0.0.1:6379> hset lock 111-11 1
--(integer) 1
--127.0.0.1:6379>
--127.0.0.1:6379> exists lock
--(integer) 1
--127.0.0.1:6379> hexists lock 3423-4234
--(integer) 0
--127.0.0.1:6379> hexists lock 111-11
--(integer) 1
--127.0.0.1:6379> hincrby lock 111-11 -1
--(integer) 0
--127.0.0.1:6379> hincrby lock 111-11 1
--(integer) 1
--127.0.0.1:6379> hincrby lock 111-11 1
--(integer) 2

-------------------------------------
-- Unlock Redis Lua cmd
-------------------------------------