-- DDL for blog_db.authors table (mysql)
-- DB : blog_db

CREATE TABLE IF NOT EXISTS blog_db.authors (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    email VARCHAR(30),
    url VARCHAR(30),
    posts VARCHAR(30)
);

-- insert data
INSERT INTO blog_db.authors
values
(1, "bill", "bill@google.com", "bill", "google.com"),
(2, "jack", "jack@uber.com", "jack", "uber.com"),
(3, "yen", "yen@google.com", "yen", "???");