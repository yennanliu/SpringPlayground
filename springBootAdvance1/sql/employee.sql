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
(1, "Bob", "bob@google.com", 1, 1),
(2, "Lee", "Lee@google.com", 0, 2),
(3, "Jill", "Jill@google.com", 0, 2);