-- DDL for city table (mysql)
-- DB : data

CREATE TABLE city (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    state VARCHAR(30),
    country VARCHAR(30)
);