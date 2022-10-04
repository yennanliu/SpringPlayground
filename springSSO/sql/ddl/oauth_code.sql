-- DDL for oauth_code table
-- DB : sso (mysql)

DROP table oauth_code;

CREATE TABLE IF NOT EXISTS oauth_code (
    code VARCHAR(256),
    authentication blob
);

-- insert data
