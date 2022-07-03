-- DDL for message table (mysql)
-- DB : data

DROP table message;

CREATE TABLE IF NOT EXISTS message (
    id VARCHAR(20),
    content VARCHAR(20),
    create_time int,
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO message(id, content, create_time)
values
("1", "yo ~~~~",20220701),
("2", "????",20220702),
("3", "hey man", 20220703);