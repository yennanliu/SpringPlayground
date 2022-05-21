-- DDL for user2 table (mysql)
-- DB : data

CREATE TABLE user2 (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    age INT(3),
    email VARCHAR(30)
);

-- insert data
INSERT INTO user2
values
(1, "Bob", 4, "bob@uber.com"),
(2, "Lily", 100, "lily.l@google.com"),
(3, "Kim", 18, "kim@google.com");