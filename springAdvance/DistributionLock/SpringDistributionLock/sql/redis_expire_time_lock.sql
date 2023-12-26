-- https://youtu.be/h_thAi4SCEQ?si=dSHt9VujL1gkRNI4&t=195

-------------------------------------
-- Redis lock with expire time demo
-------------------------------------

set lock 111 -- set lock

expire lock 20 -- set expire time

ttl lock -- check remaining time (expire time)

--127.0.0.1:6379> set lock 111
--OK
--127.0.0.1:6379>
--127.0.0.1:6379> get lock
--"111"
--127.0.0.1:6379> expire lock 20
--(integer) 1
--127.0.0.1:6379>
--127.0.0.1:6379> ttl lock
--(integer) 14
--127.0.0.1:6379> ttl lock
--(integer) 13

-- open Redis UI, then you can see TTL countdown


pexpire -- another way set expire time