-- DDL for course_type (mysql)
-- DB : course_type

CREATE TABLE IF NOT EXISTS course_type(

    type_id int PRIMARY KEY AUTO_INCREMENT,
    type_name varchar(30) not null
);

-- DML
INSERT INTO course_type(type_name)
values
("professional_compulsory"),
("professional_elective"),
("campus_elective"),
("speech");