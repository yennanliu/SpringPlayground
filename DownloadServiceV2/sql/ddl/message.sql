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
("3", "biiiiiii", 20220703),
("4", "hey google", 20220703),
("5", "kog 97", 20220704),
("6", "hey girl", 20220705),
("7", "zzzzzzz", 20220703),
("8", "wcw", 20220707),
("9", " ? ?", 20220709),
("10", "123", 20220710),
("11", "wdvfregre", 20220731);