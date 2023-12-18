-- DDL for distribution_lock (mysql)
-- DB : distribution_lock

-- create database distribution_lock;

-- drop table db_stock;

-- db_stock

CREATE TABLE IF NOT EXISTS db_stock(
    id int PRIMARY KEY AUTO_INCREMENT,
    product_code varchar(20),
    warehouse varchar(20),
    count int
);
