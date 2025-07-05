-- DDL : dept table (mysql)
-- DB : user_info

DROP table user_info;

CREATE TABLE IF NOT EXISTS user_info (
    id INT AUTO_INCREMENT,
    user_id VARCHAR(30),
    nick_name VARCHAR(50),
    mobile_no VARCHAR(50),
    password VARCHAR(50),
    is_login BOOLEAN,
    login_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    is_del BOOLEAN,
    create_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user_info(user_id, nick_name, mobile_no, password, is_login,login_time, is_del, create_time)
values
("0001", "AZ", "092376767126", "123", false, 20220101, true, 20220101),
("0011", "CZ", "092376767126", "456", false, 20220201, true, 20220101),
("0002", "JR", "092376767126", "789", false, 20220301, true, 20220101);
