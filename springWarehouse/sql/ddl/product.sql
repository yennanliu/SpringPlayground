-- DDL for product (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

-- DROP TABLE product;

CREATE TABLE IF NOT EXISTS product(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    type_id int DEFAULT 1,
    price int not null,
    merchant_id varchar(100),
    product_status  varchar(10),
    amount int DEFAULT 0
    -- constraint FK_PRODUCT_TYPE FOREIGN KEY (type_id) references product_type(type_id)
);

-- DML
-- truncate product;

INSERT INTO product(name, price, merchant_id, amount, product_status)
VALUES
("i-phone", 300, 1, 5000, "on_board"),
("cookie2", 10, 2, 5000, "new"),
("soap", 50, 3, 5000, "off_board"),
("soap", 70, 4, 5000, "off_board");