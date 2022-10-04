-- DDL for oauth_approvals table
-- DB : sso (mysql)

DROP table oauth_approvals;

CREATE TABLE IF NOT EXISTS oauth_approvals (
    id INT AUTO_INCREMENT,
    user_id VARCHAR(256),
    client_id VARCHAR(256),
    scope VARCHAR(256),
    status VARCHAR(10),
    expires_at timestamp,
    last_modified_at timestamp default current_timestamp
);

-- insert data
