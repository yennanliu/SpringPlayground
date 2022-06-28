-- DDL for history table (mysql)
-- DB : data

-- DROP table user;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT,
    name VARCHAR(20),
    age int,
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user(name, age)
values
("amy",10),
("tim",45),
("lynn",16);