-- DDL : dept table (mysql)
-- DB : webflux_test

DROP table user_info;

CREATE TABLE IF NOT EXISTS user_info (
    id INT AUTO_INCREMENT,
    mobile_no VARCHAR(50),
    sms_code VARCHAR(50),
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO user_info(mobile_no, sms_code)
values
("09234716126", "123"),
("092376767126", "777"),
("000000000000", "999");
