-- DDL for download_status (mysql)
-- DB : warehouse_system

-- create database warehouse_system;

CREATE TABLE IF NOT EXISTS download_status(
    id int PRIMARY KEY AUTO_INCREMENT,
    download_url varchar(100),
    status varchar(10),
    create_time datetime,
    complete_time datetime
);

INSERT INTO download_status(download_url, status, create_time)
VALUES
("some_url", "pending", now()),
("some_url", "failed", now()),
("some_url", "completed", now());