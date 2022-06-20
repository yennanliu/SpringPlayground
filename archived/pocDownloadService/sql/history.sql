-- DDL for history table (mysql)
-- DB : data

-- DROP TABLE history;

CREATE TABLE IF NOT EXISTS history (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    userList VARCHAR(50),
    reportField VARCHAR(50),
    exportType VARCHAR(20),
    startTime Int(10),
    endTime Int(10),
    status VARCHAR(10)
);

-- insert data
INSERT INTO history
values
(1, "user-a,user-b", "col-a,col-b", "transaction",100,200,"running"),
(2, "user-z,user-g", "col-b", "deposit",100,1000,"completed"),
(3, "user-d", "col-f", "withdraw",900,1400,"failed");