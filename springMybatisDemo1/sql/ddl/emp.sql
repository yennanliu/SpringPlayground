-- DDL : emp table (mysql)
-- DB : mybatis

DROP table emp;

CREATE TABLE IF NOT EXISTS emp (
    eid INT AUTO_INCREMENT,
    emp_name VARCHAR(20),
    age int,
    sex char,
    email VARCHAR(20),
    did int,
    PRIMARY KEY (eid)
);

-- insert data
INSERT INTO emp(emp_name, age, sex, email, did)
values
("jack", 29, 1, "jack@fb.com", 1),
("dim", 29, 1, "dim@fb.com",2),
("ann", 29, 1, "ann@fb.com",1),
("Agnis", 29, 1, "Agnis@fb.com",2),
("mary", 29, 1, "mary@fb.com",3);
