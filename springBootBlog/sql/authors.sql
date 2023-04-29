-- DDL for blog_db.authors table (mysql)
-- DB : blog_db

CREATE TABLE IF NOT EXISTS blog_db.authors (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    email VARCHAR(30),
    url VARCHAR(30),
    create_time datetime,
    update_time datetime
);

-- insert data
INSERT INTO blog_db.authors(`id`,`email`,`name`,`url`, `create_time`, `update_time`)
values
(2,  "bill@google.com", "bill", "" ,  now(), now()),
(3,  "jack@uber.com", "jack", "" , now(), now()),
(4, "yen@google.com", "yen", "" , now(), now());