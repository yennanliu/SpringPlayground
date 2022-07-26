-- DDL : comment table (mysql)
-- DB : mybatis

DROP table comment;

CREATE TABLE IF NOT EXISTS comment (
    id INT AUTO_INCREMENT,
    content VARCHAR(20),
    commentDate datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO comment(content)
values
("a comment."),
("aother comment."),
(".....");
