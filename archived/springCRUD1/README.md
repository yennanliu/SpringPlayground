# Spring CRUD
> Spring CRUD

### Commands
```sql
#----------------------
# SQL DDL
#----------------------

# https://github.com/yennanliu/utility_shell/blob/master/mysql/mac_install_sql.sh

# create db
create database sales;

# creat table
use sales;

drop table user;

CREATE TABLE `user` (
  `id` SERIAL NOT NULL AUTO_INCREMENT,
  `email` varchar(20) NOT NULL,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# insert data
insert into user VALUES ('001','mary@gmail.com','mary');
insert into user VALUES ('002','jack@gmail.com','jack');
insert into user VALUES ('003','amy@gmail.com','amy');
```

### Ref
- Code
	- https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-crud
- Blog
	- https://www.baeldung.com/spring-boot-crud-thymeleaf