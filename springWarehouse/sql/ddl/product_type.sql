-- DDL for product_type (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS product_type(

    type_id int PRIMARY KEY AUTO_INCREMENT,
    type_name varchar(30) not null
);

-- DML
INSERT INTO product_type(type_name)
values
("fmcg"),
("mobile"),
("hardware"),
("food");
