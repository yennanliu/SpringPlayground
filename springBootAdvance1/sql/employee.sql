-- DDL for employee table (mysql)
-- DB : spring_cache

CREATE TABLE IF NOT EXISTS employee (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    lastName VARCHAR(30),
    email VARCHAR(30),
    gender INT(1),
    dId INT(11)
);

-- insert data
INSERT INTO employee
values
(1, "Bob", "Dylan", "bob@google.com", 1, 1),
(1, "Lee", "Yoo", "Lee@google.com", 0, 2),
(1, "Jill", "Smith", "Jill@google.com", 0, 2);