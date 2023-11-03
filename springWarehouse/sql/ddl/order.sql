-- DDL for order (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS orders (
    id varchar(36) PRIMARY KEY,
    merchant_id int,
    product_id int,
    amount int,
    status varchar(10),
    create_time datetime,
    update_time datetime
);


INSERT INTO orders(id, merchant_id, product_id, amount, status, create_time, update_time)
VALUES
("213ac24efgefdf84", 1, 1, 1, "completed", now(), now()),
("d42b3224-b715-4gfgf-3b2eccc7a", 2, 2, 1, "cancelled", now(), now()),
("d42b322;;4-b715-4gfgf-3b2eccc7a", 2, 2, 1, "cancelled", now(), now()),
("-b715-4gfg;;f-3b2eccc7a", 2, 2, 1, "cancelled", now(), now()),
("-b715-4gfgf-", 2, 2, 1, "cancelled", now(), now()),
("d42b3224--4gfgf-3b2eccc7a", 2, 2, 1, "cancelled", now(), now()),
(uuid(), 1, 3, 1, "pending", now(), now());