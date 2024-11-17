------------------------------------------------------------
-- Demo how transactional make local JVM local failed
------------------------------------------------------------

-- https://youtu.be/pD8bEeq9q_U?si=XXT73vZJj67RGWLG&t=403

-------------------------
-- open Mysql client 1
-------------------------

-- open transactional
begin;

select * from db_stock where product_code = 'prod-1';

update db_stock set count = 20  where product_code = 'prod-1';

select * from db_stock where product_code = 'prod-1';

--+----+--------------+-----------+-------+
--| id | product_code | warehouse | count |
--+----+--------------+-----------+-------+
--|  1 | prod-1       | w-1       |    20 |
--+----+--------------+-----------+-------+

-------------------------
-- open Mysql client 2
-------------------------

-- open transactional
begin;

select * from db_stock where product_code = 'prod-1';
-- result is STILL the record before client 1 changed (since client 1 is NOT YET commit)

--+----+--------------+-----------+-------+
--| id | product_code | warehouse | count |
--+----+--------------+-----------+-------+
--|  1 | prod-1       | w-1       |  1836 |
--+----+--------------+-----------+-------+