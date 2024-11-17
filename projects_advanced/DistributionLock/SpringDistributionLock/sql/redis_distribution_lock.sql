-- https://youtu.be/YtkozzqKYOI?si=BKOfPojNKSR3FXxG&t=118

-------------------------
-- set
-------------------------

--127.0.0.1:6379> set k1 v1
--OK
--127.0.0.1:6379> get k1
--"v1"
--127.0.0.1:6379> set k1 v2
--OK
--127.0.0.1:6379> get k1
--"v2"

-------------------------
-- setnx
-------------------------

-- setnx VS set
--      setnx : CAN ONLY setup a key if such key NOT existed, or set up failed
--              -> consider if there are 100 requests coming in
--      set : can still set up no matter key existed or not
setnx lock 111 -- set lock
get lock -- get lock
del lock -- release lock

--127.0.0.1:6379> setnx lock 111
--(integer) 1
--127.0.0.1:6379> setnx lock 222
--(integer) 0  -- setting failed
--127.0.0.1:6379> get lock
--"111"
--127.0.0.1:6379> del lock
--(integer) 1
--127.0.0.1:6379> get lock
--(nil)
--127.0.0.1:6379>  setnx lock 222
--(integer) 1
--127.0.0.1:6379> get lock
--"222"