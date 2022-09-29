-- DDL for users (mysql)
-- DB : course_type

-- create database course_system;

CREATE TABLE IF NOT EXISTS users(

    user_no int PRIMARY KEY AUTO_INCREMENT,
    user_pwd varchar(1000) not null,
    user_name varchar(100) not null
);

-- DML
INSERT INTO users(user_pwd, user_name)
values
('123', 'jack'),
('123', 'lynn'),
('123', 'ann');