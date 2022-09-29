-- DDL : dept table (mysql)
-- DB : user_system

DROP table user_sms_code;

CREATE TABLE IF NOT EXISTS user_sms_code (
    id INT AUTO_INCREMENT,
    mobile_no VARCHAR(50),
    sms_code VARCHAR(50),
    send_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    create_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user_sms_code(mobile_no, sms_code, send_time, create_time)
values
("09234716126", "hello!!", 20220709, 20220709),
("092376767126", "kisss!!", 20001220, 20220901),
("000000000000", "yoyo!!", 20220901, 20001220);
