---------------------------------------------------------
-- Redis ReentrantLock lock implementation with Lua script
---------------------------------------------------------

-- https://youtu.be/V-OREDbG2UA?si=dNHrBr83Gq5dR8RY

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

-- if lock not existed
if redis.call('exists', 'lock') = 0
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


-------------------------------------
-- Unlock
-------------------------------------

-------------------------------------
-- Unlock Redis Lua cmd
-------------------------------------