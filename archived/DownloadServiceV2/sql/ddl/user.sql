-- DDL for history table (mysql)
-- DB : data

DROP table user;

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
("lynn",16),
("kevin",32),
("sam",1),
("john",99),
("jim",55),
("shin",23),
("yamazaki",12),
("ruby",58),
("yak",4);