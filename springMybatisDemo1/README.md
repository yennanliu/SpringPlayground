# springMybatisDemo1
> Mybatis demo with spring boot

- Features
    - myBatis 
        - mapping
        - resultMap
        - dynamic SQL
    - paging

## Steps

## Run

<details>
<summary>App</summary>

- prerequisite
    - plz run all DDL under /sql/ddl

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Test | GET | test | http://localhost:8888/test |
| get all users | GET | test | http://localhost:8888/user/total |
| get users by page | POST | test | curl -X 'POST' 'http://localhost:8888/user/get_user?page=1&size=4' -H 'accept: */*' -d ''|

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Swagger UI | GET | test | http://localhost:8888/swagger-ui/index.html |

## Important Concepts

- MyBatis
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1//doc/pic/mybatis_xml_1.png">

- SQL session
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/sqlsession.png">

- log4j logging
    - priority : FATAL > ERROR > WARN > INFO > DEBUG
    - log info becomes more detail from left to right (FATAL -> ... -> DEBUG)

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=PG1lABauiSc&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=1