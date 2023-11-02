-- DDL for comment table

INSERT INTO comment(`author_id`,`post_id`,`comment_content`, `create_time`, `update_time`)
values
(1, 1,  "some comment", now(), now()),
(2, 2,  "some comment", now(), now()),
(3, 3,  "some comment", now(), now());
