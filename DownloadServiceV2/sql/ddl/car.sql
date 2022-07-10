-- DDL for car table (mysql)
-- DB : data

DROP table car;

CREATE TABLE IF NOT EXISTS car (
    id INT AUTO_INCREMENT,
    brand VARCHAR(20),
    price int,
    PRIMARY KEY (id)
);

-- insert data
INSERT INTO car(brand, price)
values
("tesla",1000),
("ford",20),
("benz",3000),
("bmw",2000),
("lexus",900),
("toyota",200),
("suzuki",200),
("honda",100),
("audi",2000),
("brand-a",1000),
("brand-b",1000);