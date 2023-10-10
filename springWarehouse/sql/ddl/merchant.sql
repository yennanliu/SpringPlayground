-- DDL for merchant (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS merchant(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    city varchar(10),
    type varchar(10),
    status varchar(1)
);

INSERT INTO merchant(name, city, type, status)
VALUES
("company-a", "tokyo", "regular", "0"),
("company-b", "london", "regular", "0"),
("company-c", "new york", "new", "0");