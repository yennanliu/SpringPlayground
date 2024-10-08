-- DDL for product (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS product(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    type_id int not null,
    price int not null,
    merchant_id varchar(100),
    product_status  varchar(10),
    amount int DEFAULT 0,
    constraint FK_PRODUCT_TYPE FOREIGN KEY (type_id) references product_type(type_id)
);

-- DML
INSERT INTO product(name, type_id, price, merchant_id, amount, product_status)
VALUES
("i-phone", 2, 300, 1, 3, "on_board"),
("cookie2", 4, 10, 2, 2, "new"),
("soap", 1, 50, 1, 1, "off_board");