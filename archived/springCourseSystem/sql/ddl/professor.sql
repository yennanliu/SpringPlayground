-- DDL for professor (mysql)
-- DB : course_system
-- create database course_system;

CREATE TABLE IF NOT EXISTS professor(
   id varchar(100) primary key comment 'id',
   name varchar(500) comment 'name',
   department varchar(100) comment 'department'
)engine=innodb default charset=utf8;

-- DML
INSERT INTO professor(id, name, department)
values
('1001', 'jack', 'CS'),
('102', 'Linda', 'Physics'),
('103', 'Amy', 'EE'),
('104', 'jessie', 'CHE'),
('105', 'lynn', 'ME'),
('106', 'jim', 'JP'),
('107', 'ken', 'CS');

-- random ID
INSERT INTO professor(id, name, department)
values
( FLOOR(534 + (RAND() * 61)), 'jack', 'CS'),
(FLOOR(345 + (RAND() * 61)), 'Linda', 'Physics'),
(FLOOR(456 + (RAND() * 61)), 'Amy', 'EE'),
(FLOOR(756 + (RAND() * 61)), 'jessie', 'CHE'),
(FLOOR(87 + (RAND() * 61)), 'lynn', 'ME');