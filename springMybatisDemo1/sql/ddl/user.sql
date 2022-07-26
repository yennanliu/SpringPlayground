-- DDL : user table (mysql)
-- DB : mybatis

DROP table user;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT,
    name VARCHAR(20),
    age int,
    base_location VARCHAR(20),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user(name, age, base_location)
values
("amy",10,'JP'),
("tim",45,'TW'),
("lynn",16,'US'),
("kevin",32,'FR'),
("sam",1,'JP'),
("john",99,'US'),
("jim",55,'ID'),
("shin",23,'UK'),
("yamazaki",12,'TW'),
("ruby",58,'TW'),
("yak",4,'UK');