-- DDL for markdownblogdb.authors table (mysql)
-- DB : markdownblogdb

CREATE TABLE IF NOT EXISTS markdownblogdb.posts (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    email VARCHAR(30),
    url VARCHAR(30),
    posts VARCHAR(30)
);

-- insert data
INSERT INTO markdownblogdb.posts
values
(1, "bill", "bill@google.com", "xxx", "fwefewf"),
(2, "jack", "jack@google.com", "wefew", "fwefergregerewf"),
(3, "elon", "elon@google.com", "wfwe", "ergre");