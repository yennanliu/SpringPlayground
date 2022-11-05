-- DDL : my_user table (mysql)
-- DB : mybatis

-- https://www.youtube.com/watch?v=xVdgzSkU6po&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=10

DROP table my_user;

CREATE TABLE IF NOT EXISTS my_user (
    id INT AUTO_INCREMENT,
    username VARCHAR(20),
    password VARCHAR(20),
    age int,
    sex char,
    email VARCHAR(20),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO my_user(username, password, age, sex, email)
values
("amy",123,10,0,"amy@google.com"),
("jack",456,36,0,"jack@google.com"),
("lynn",888,20,0,"lynn@google.com");