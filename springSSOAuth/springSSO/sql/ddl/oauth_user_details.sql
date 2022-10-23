-- DDL for oauth_user_details table
-- DB : sso (mysql)

DROP table oauth_user_details;

CREATE TABLE IF NOT EXISTS oauth_user_details (
    id INT AUTO_INCREMENT,
    user_name VARCHAR(256) not null comment 'id',
    password VARCHAR(256) not null default '',
    salt VARCHAR(256) not null default '' comment 'MDS key for user pwd',
    nick_name VARCHAR(256)  default '',
    mobile VARCHAR(11) not null default '',
    gender int not null default 3,
    authorities VARCHAR(256) not null default 'all',
    not_expired boolean default true,
    not_locked boolean default true,
    credentials_non_expired boolean default true,
    enabled boolean default true,
    create_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    create_by VARCHAR(100) not null default 'system',
    update_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    update_by VARCHAR(100) not null default 'system',
    PRIMARY KEY (id)
) engine=innodb default charset=utf8;

-- insert data
