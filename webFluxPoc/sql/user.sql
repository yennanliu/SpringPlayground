-- DDL : dept table (mysql)
-- DB : webflux_test

DROP table user_info;

CREATE TABLE IF NOT EXISTS user_info (
    id INT AUTO_INCREMENT,
    mobile_no VARCHAR(50),
    sms_code VARCHAR(50),
    send_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    create_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user_info(mobile_no, sms_code, send_time, create_time)
values
("09234716126", "123", 20220709, 20220709),
("092376767126", "777", 20001220, 20220901),
("000000000000", "999", 20220901, 20001220);
