-- DDL for payment table
-- DB : spring_cloud
-- table : payment
-- https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10

CREATE TABLE `payment` (

`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`serial` varchar(200) DEFAULT "",
PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8;


INSERT INTO `payment` (`serial`)
VALUES 
('142345'),
('945674564'),
('45645');