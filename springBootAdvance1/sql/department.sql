-- DDL for department table (mysql)
-- DB : spring_cache

CREATE TABLE IF NOT EXISTS department (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    departmentName VARCHAR(30)
);

-- insert data
INSERT INTO department
values
(1, "TECH"),
(2, "HR"),
(3, "MKT");