-- DDL for users (mysql)
-- DB : course_type

CREATE TABLE IF NOT EXISTS users(

	user_no varchar(20) AUTO_INCREMENT,
	user_pwd varchar(1000) not null,
	user_name varchar(100) not null
);