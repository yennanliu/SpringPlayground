-- DDL for oauth_access_token table
-- DB : sso (mysql)

DROP table oauth_access_token;

CREATE TABLE IF NOT EXISTS oauth_access_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token_id VARCHAR(256),
    token blob comment 'java serialized binary data',
    authentication_id VARCHAR(256),
    user_name VARCHAR(256),
    client_id VARCHAR(256),
    authentication blob,
    refresh_token VARCHAR(256)
);

-- insert data
