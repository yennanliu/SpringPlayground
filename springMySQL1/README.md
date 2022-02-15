# SpringMySQL1
> Spring MySQL CRUD demo

### Commands

```bash
#----------------------
# API
#----------------------

curl http://localhost:8090/
```

```bash
# go to mysql shell
mysql -u root -p
```

```sql
#----------------------
# SQL DDL
#----------------------

# create db
create database sales;

# creat table
use sales;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `brand` varchar(45) NOT NULL,
  `madein` varchar(45) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# insert data
insert into product VALUES (1000, 'mary', 'benz', "JP", 1000);
insert into product VALUES (1002, 'tom', 'tesla', "US", 3000);
insert into product VALUES (1003, 'jack', 'lexus', "JP", 2000);
```

### Progress
- [progress.txt](https://github.com/yennanliu/springPlayground/blob/main/springMySQL1/doc/progress.txt)

### Ref
- Spring MVC + mysql
    - https://www.codejava.net/frameworks/spring-boot/spring-boot-crud-example-with-spring-mvc-spring-data-jpa-thymeleaf-hibernate-mysql#Database
- Mysql install @ mac
    - https://github.com/yennanliu/utility_shell/blob/master/mysql/mac_install_sql.sh