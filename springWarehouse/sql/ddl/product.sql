-- DDL for product (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS product(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    type_id int not null,
    price int not null,
    product_status varchar(1) not null,
    constraint FK_PRODUCT_TYPE FOREIGN KEY (type_id) references product_type(type_id)
);

-- DML
INSERT INTO product(name, type_id, price, product_status)
VALUES
("i-phone", 2, 300, "0"),
("cookie", 4, 10, "0"),
("soap", 1, 50, "0");