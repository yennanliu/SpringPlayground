-- DDL for order (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS orders (
    id varchar(36) PRIMARY KEY,
    merchant_id int,
    product_id int,
    amount int,
    create_time datetime,
    update_time datetime
);


INSERT INTO orders(id, merchant_id, product_id, amount, create_time, update_time)
VALUES
("213ac245-7a2a-453b-a4a7-149426b13f84", 1, 1, 1, now(), now()),
("d42b3224-b715-46af-9294-3b2ecc6ccc7a", 2, 2, 1, now(), now()),
(uuid(), 1, 3, 1, now(), now());