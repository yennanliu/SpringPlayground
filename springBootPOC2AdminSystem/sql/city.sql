-- DDL for city table (mysql)
-- DB : data

CREATE TABLE IF NOT EXISTS city (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    state VARCHAR(30),
    country VARCHAR(30)
);

-- insert data
INSERT INTO city
values
(1, "Bob", "US", "NY"),
(2, "Lily", "TW", "TPE"),
(3, "Kim", "UK", "LDN"),
(4, "Ana", "FR", "PARIS"),
(5, "Jack", "JP", "TOKYO");