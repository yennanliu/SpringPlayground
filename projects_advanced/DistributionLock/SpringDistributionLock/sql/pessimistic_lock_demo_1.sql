------------------------------------------------------------
-- Demo how to setup pessimistic lock in mysql
------------------------------------------------------------

--https://youtu.be/tIIOSs4Wd-0?si=VgnNHLbHXvH4x4Sv&t=108

-------------------------
-- open Mysql client 1
-------------------------

-- open transactional
begin;

-- DON'T USE THIS (without pessimistic lock)
-- select * from db_stock where product_code = 'prod-1';

-- USE THIS (with pessimistic lock)
select * from db_stock where product_code = 'prod-1' for update;


--mysql> select * from db_stock where product_code = 'prod-1';
--+----+--------------+-----------+-------+
--| id | product_code | warehouse | count |
--+----+--------------+-----------+-------+
--|  1 | prod-1       | w-1       |  5000 |
--+----+--------------+-----------+-------+
--1 row in set (0.00 sec)
--
--mysql>
--mysql> select * from db_stock where product_code = 'prod-1' for update;
--+----+--------------+-----------+-------+
--| id | product_code | warehouse | count |
--+----+--------------+-----------+-------+
--|  1 | prod-1       | w-1       |  4998 |
--+----+--------------+-----------+-------+
--1 row in set (0.00 sec)


-------------------------
-- open Mysql client 2
-------------------------


-- update count
update db_stock set count = 4998  where product_code = 'prod-1';

-- After client 1 run "select * from db_stock where product_code = 'prod-1' for update;"
-- client 2's op (as below) will be LOCKED

--mysql>
--mysql> update db_stock set count = 4998  where product_code = 'prod-1';

