-- DDL for users (mysql)
-- DB : course_system
-- create database course_system;

CREATE TABLE IF NOT EXISTS users(
   user_no varchar(100) primary key comment 'user account',
   user_pwd varchar(500) comment 'user pwd',
   user_name varchar(500) comment 'user name'
)engine=innodb default charset=utf8;

-- DML
INSERT INTO users(user_no, user_pwd, user_name)
values
('00101', '123', 'yen'),
('00102', '123', 'ann'),
('00103', '123', 'jack');