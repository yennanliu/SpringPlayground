-- DDL for t_book table (mysql)
-- DB : mytest

CREATE TABLE IF NOT EXISTS t_book(
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    author VARCHAR(30)
);

-- insert data
INSERT INTO t_book
values
(1, "python cookbook", "tom"),
(2, "scala doc", "jim"),
(3, "java bible", "lynn");