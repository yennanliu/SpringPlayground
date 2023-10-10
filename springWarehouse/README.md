# Spring Warehouse System
> Build a Warehouse admin system with Spring boot

## Steps

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

## Ref

- Existing product
	- https://www.tmserp.com.tw/industry_07.aspx?gclid=Cj0KCQjw7JOpBhCfARIsAL3bobdw6Ae8MEQjsVEgMH67A899qtXm91vq5IU3BQqIL3n6EEFMNk3IfjgaAhYNEALw_wcB
	- https://blog.csdn.net/QGhurt/article/details/117508945
		- code : https://gitee.com/java668/Invoicing
	- https://blog.csdn.net/shunyache3481/article/details/125003417

## TODO

1. Add spec doc, feature design
2. Add module:
	- buying
	- storage
	- sales
	- report
	- resource management
	- system admin
2. Add amount to Product bean
3. Add batch add/delete