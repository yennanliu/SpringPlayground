------------------------------------------------------------
-- Demo for DEADLOCK which may caused by pessimistic lock in mysql
------------------------------------------------------------

-- https://youtu.be/MhF27ngwmFI?si=GItp1tKgA4lBnAcj
-- pessimistic lock : select ... for update;


-------------------------
-- open Mysql client 1
-------------------------

-- start transaction
begin;

select * from db_stock where id = 1 for update;

select * from db_stock where id = 2 for update;


-------------------------
-- open Mysql client 2
-------------------------

-- start transaction
begin;

select * from db_stock where id = 2 for update;

-- face Deadlock when run below cmd
select * from db_stock where id = 1 for update;

--mysql> select * from db_stock where id = 1 for update;
--ERROR 1213 (40001): Deadlock found when trying to get lock; try restarting transaction