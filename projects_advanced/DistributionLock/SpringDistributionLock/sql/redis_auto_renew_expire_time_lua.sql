------------------------------------------------------------
-- Lua script automatically renew lock expire time
------------------------------------------------------------

-- https://youtu.be/Eom2zGivV7M?si=kdgSkZHnbRyeZe9R&t=789

-------------------------------------
-- Lua script
-------------------------------------

if redis.call('HEXISTS', 'lock', uuid) == 1
then
    return redis.call('EXPIRE', 'lock', 30) -- will return 1
else
    return 0
end

-- replace with param
if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1
then
    return redis.call('EXPIRE', KEYS[1],  ARGV[2])
else
    return 0
end

key: lock
arg: uuid 30

-- one line

if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1 then return redis.call('EXPIRE', KEYS[1],  ARGV[2]) else return 0 end

-- with EVAL

EVAL "if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1 then return redis.call('EXPIRE', KEYS[1],  ARGV[2]) else return 0 end" 1 lock 777-77 30


--127.0.0.1:6379> hset lock  777-77 3
--(integer) 1
--127.0.0.1:6379> EVAL "if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1 then return redis.call('EXPIRE', KEYS[1],  ARGV[2]) else return 0 end" 1 lock 777-77 30
--(integer) 1
--127.0.0.1:6379>