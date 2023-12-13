-- DML for distribution_lock (mysql)
-- DB : distribution_lock

-- create database distribution_lock;

-- dbs_stock

INSERT INTO dbs_stock(product_code, warehouse, count)
VALUES
("prod-1", "w-1", 1000),
("prod-2", "w-2", 2000),
("prod-3", "w-3", 3000);