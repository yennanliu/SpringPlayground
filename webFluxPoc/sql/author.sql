-- DDL : dept table (mysql)
-- DB : webflux_test

DROP table author;

CREATE TABLE IF NOT EXISTS author (
    id INT AUTO_INCREMENT,
    name VARCHAR(50),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO author(name)
values
("jack"),
("rose"),
("kim");
