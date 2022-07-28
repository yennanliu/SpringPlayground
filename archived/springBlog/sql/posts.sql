-- DDL for markdownblogdb.posts table (mysql)
-- DB : markdownblogdb

CREATE TABLE IF NOT EXISTS markdownblogdb.posts (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30),
    content VARCHAR(30),
    synopsis VARCHAR(30),
    author_id VARCHAR(30),
    localDateTime VARCHAR(30)
);

-- insert data
INSERT INTO markdownblogdb.posts
values
(1, "title A", "HELLO", "synopsis",  "user_a", 20220101),
(2, "title b", "HELLO b", "synopsis", "user_b", 20220102),
(3, "title c", "HELLO c", "synopsis", "user_c", 20220103);