-- DDL for course (mysql)
-- DB : course_system

-- create database course_system;

CREATE TABLE IF NOT EXISTS course(
    course_no varchar(50) PRIMARY KEY,
    course_name varchar(100) not null,
    course_hours int not null,
    type_id int not null,
    course_status varchar(1) not null,
    course_reqs varchar(20) not null,
    course_point decimal(3,1),
    course_memo varchar(1000),
    course_textbook_pic mediumblob,
    constraint FK_COURSE_TYPE FOREIGN KEY (type_id) references course_type(type_id)
);

INSERT INTO course(course_no, course_name, course_hours, type_id, course_status, course_reqs, course_point, course_memo, course_textbook_pic)
VALUES
(100, "course-0", 1, 1, 0, "reqs", 2, "memo0..", ""),
(101, "course-1", 1, 1, 0, "reqs", 2, "memo1..", ""),
(102, "course-2", 1, 1, 0, "reqs", 2, "memo2..", ""),
(103, "course-3", 1, 1, 0, "reqs", 2, "memo3..", ""),
(104, "course-4", 1, 1, 0, "reqs", 2, "memo4..", ""),
(105, "course-5", 1, 1, 1, "reqs", 2, "memo5..", ""),
(106, "course-6", 1, 1, 1, "reqs", 2, "memo6..", ""),
(107, "course-7", 1, 1, 1, "reqs", 2, "memo7..", ""),
(108, "course-8", 1, 1, 1, "reqs", 2, "memo8..", ""),
(109, "course-9", 1, 1, 1, "reqs", 2, "memo9..", ""),
(110, "course-10", 1, 1, 1, "reqs", 2, "memo10..", "");