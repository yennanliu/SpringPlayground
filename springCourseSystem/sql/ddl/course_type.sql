-- DDL for course_type (mysql)
-- DB : course_type

CREATE TABLE IF NOT EXISTS course_type(

	type_id int PRIMARY KEY AUTO_INCREMENT,
	type_name varchar(30) not null
);