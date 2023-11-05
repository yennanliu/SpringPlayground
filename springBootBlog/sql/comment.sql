-- DDL for comment table

INSERT INTO comment(`user_name`,`post_id`,`comment_content`, `create_time`, `update_time`)
values
("mask", 1,  "some comment", now(), now()),
("kim", 2,  "some comment", now(), now()),
("yuri", 3,  "some comment", now(), now()),
("suzuki", 1,  "ahahahahahahaah", now(), now());
