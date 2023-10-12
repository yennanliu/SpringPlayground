# Spring Warehouse System

Build a Warehouse admin system with Spring boot.
This system is for internal operation users. offers product, merchant, and order management.
Warehouse data are maintained, managed via this app

- Data Model
<p align="center"><img src ="./doc/pic/data_model.png"></p>

- Main functionality
  - Add, remove, modify on *product* (storage), type, merchant, order

- Terms
  - Product : goods storage in warehouse
  - Merchant : business (company) who owns product
  - Orders : a transaction, product from warehouse will be sent to client 

## Tech

- Java
- Spring boot
- mybatisplus
- thymeleaf
- Mysql


## Run

<details>
<summary>App</summary>

- Run DDL first
	- all SQL files under `/sql/ddl`

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar target/springWarehouse-0.0.1-SNAPSHOT.jar
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:7777/ | Get | Home page || |
| http://localhost:7777/swagger-ui/index.html | Get | API doc page || |

## Important Concepts

## TODO

1. Add spec doc, feature design
2. Add module:
    - buying
    - storage
    - sales
    - report
    - resource management
    - system admin
3. Add amount to Product bean
4. Add batch add/delete
5. Code refactor
6. Unit test
7. Deployment
   1. DockerFile
   2. CI/CD
   3. logs


## Ref

- Existing product
	- https://www.tmserp.com.tw/industry_07.aspx?gclid=Cj0KCQjw7JOpBhCfARIsAL3bobdw6Ae8MEQjsVEgMH67A899qtXm91vq5IU3BQqIL3n6EEFMNk3IfjgaAhYNEALw_wcB
	- https://blog.csdn.net/QGhurt/article/details/117508945
		- code : https://gitee.com/java668/Invoicing
	- https://blog.csdn.net/shunyache3481/article/details/125003417
	- https://blog.csdn.net/Shirley_G_Zhang/article/details/79321538?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-79321538-blog-125003417.235%5Ev38%5Epc_relevant_anti_t3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-79321538-blog-125003417.235%5Ev38%5Epc_relevant_anti_t3&utm_relevant_index=9
	- https://blog.csdn.net/sD7O95O/article/details/128681260?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-128681260-blog-125003417.235^v38^pc_relevant_anti_t3&spm=1001.2101.3001.4242.1&utm_relevant_index=3