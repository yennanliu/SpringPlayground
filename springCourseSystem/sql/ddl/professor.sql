-- DDL for professors (mysql)
-- DB : course_system
-- create database course_system;

CREATE TABLE IF NOT EXISTS professors(
   id varchar(100) primary key comment 'id',
   name varchar(500) comment 'name',
   department varchar(100) comment 'department'
)engine=innodb default charset=utf8;

-- DML
INSERT INTO professors(id, name, department)
values
('100', 'jack', 'CS'),
('102', 'Linda', 'Physics'),
('103', 'Amy', 'EE');