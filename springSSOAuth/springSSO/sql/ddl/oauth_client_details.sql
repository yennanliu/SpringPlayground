-- DDL for oauth_client_details table
-- DB : sso (mysql)

DROP table oauth_client_details;

CREATE TABLE IF NOT EXISTS oauth_client_details (
    id INT AUTO_INCREMENT,
    client_id VARCHAR(256) PRIMARY KEY,
    resource_ids VARCHAR(256),
    client_secret VARCHAR(256),
    scope VARCHAR(256),
    authorized_grant_types VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities VARCHAR(256),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(256)
);

-- insert data
