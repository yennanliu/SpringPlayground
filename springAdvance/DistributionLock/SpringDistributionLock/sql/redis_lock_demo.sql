-- https://youtu.be/QxIAt_xgfA0?si=H4lEG_11e5n8ld7Y&t=108

-------------------------
-- open Redis client 1
-------------------------

-- 1) watch : monitor 1 or multiple key value,
--         if key val changed before transaction execution (exec),
--         then cancel transaction exec
--
-- 2) multi : open a transaction
-- 3) exec :  execute a transaction

redis-cli

set stock 5000

watch stock -- add lock

multi -- start transaction

get stock

set stock 5000

exec -- execution failed

--127.0.0.1:6379(TX)>  get stock
--QUEUED
--127.0.0.1:6379(TX)>  set stock 5000
--QUEUED
--127.0.0.1:6379(TX)>
--127.0.0.1:6379(TX)> exec
--(nil)   -- execution failed

-------------------------
-- open Redis client 1
-------------------------

redis-cli

set stock 3000