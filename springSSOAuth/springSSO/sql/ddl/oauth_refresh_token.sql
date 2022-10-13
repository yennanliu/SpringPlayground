-- DDL for oauth_refresh_token table
-- DB : sso (mysql)

DROP table oauth_refresh_token;

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    id INT AUTO_INCREMENT,
    token_id VARCHAR(256),
    token blob comment 'java serialized binary data',
    authentication binary
);

-- insert data
