-- DDL for t_account table (mysql)
-- DB : mytest

CREATE TABLE IF NOT EXISTS t_account(
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    balance INT(10),
    user_name VARCHAR(30)
);

-- insert data
INSERT INTO t_account
values
(1, 100, "tom"),
(2, 0, "jack"),
(3, 30, "lee");