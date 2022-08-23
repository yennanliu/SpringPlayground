-- DDL : dept table (mysql)
-- DB : mybatis

DROP table dept;

CREATE TABLE IF NOT EXISTS dept (
    did INT AUTO_INCREMENT,
    dept_name VARCHAR(20),
    PRIMARY KEY (did)
);

-- insert data
INSERT INTO dept(dept_name)
values
("sales"),
("op"),
("mkt"),
("tech");
