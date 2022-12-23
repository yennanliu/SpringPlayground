-- DDL for blog_db.posts table (mysql)
-- DB : blog_db

CREATE TABLE IF NOT EXISTS blog_db.posts (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(30),
    date_time VARCHAR(30),
    synopsis VARCHAR(30),
    title VARCHAR(30),
    author_id VARCHAR(30)
);

-- insert data
INSERT INTO blog_db.posts
values
(1, "title A", "HELLO", "synopsis",  "user_a", 20220101),
(2, "title b", "HELLO b", "synopsis", "user_b", 20220102),
(3, "title c", "HELLO c", "synopsis", "user_c", 20220103);