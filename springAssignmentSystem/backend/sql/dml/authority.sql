# DML for authority table
# https://youtu.be/_L46CaEI490?si=6hfnXT88mwQ2nM_m&t=1460

/** users */
INSERT INTO users(password, username, email, name)
VALUES
( '123', 'admin', 'xxx@gmail.com', 'admin'),
( '123', 'code_reviewer', 'xxx@gmail.com', 'code_reviewer');

/** authority */
INSERT INTO authority(authority, user_id)
VALUES
('ROLE_STUDENT', 1),
('CODE_REVIEWER', 2);