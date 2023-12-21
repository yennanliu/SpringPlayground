------------------------------------------------------------
-- Demo how to setup Optimistic lock in mysql
------------------------------------------------------------

-- https://youtu.be/y7blICVJ2i0?si=6pPvbxeUM7ulTECv&t=544

select * from db_stock where product_code = 'prod-1';

update db_stock set count=4999, version=version+1 where product_code = 'prod-1' and version=0;

--mysql> update db_stock set count=4999, version=version+1 where product_code = 'prod-1' and version=0;
--Query OK, 1 row affected (0.01 sec)
--Rows matched: 1  Changed: 1  Warnings: 0

--mysql> update db_stock set count=4999, version=version+1 where product_code = 'prod-1' and version=0;
--Query OK, 0 rows affected (0.01 sec)
--Rows matched: 0  Changed: 0  Warnings: 0