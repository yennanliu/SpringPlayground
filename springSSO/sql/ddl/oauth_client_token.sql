-- DDL for oauth_client_token table
-- DB : sso (mysql)

DROP table oauth_client_token;

CREATE TABLE IF NOT EXISTS oauth_client_token (
    id INT AUTO_INCREMENT,
    token_id VARCHAR(256),
    token blob comment 'java serialized binary data',
    authentication_id VARCHAR(256) PRIMARY KEY,
    user_name VARCHAR(256),
    client_id VARCHAR(256)
);

-- insert data
