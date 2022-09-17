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

- MyBatis mapping
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1//doc/pic/mybatis_xml_1.png">

- MyBatis variable 
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1//doc/pic/mybatis_variable.png">

- Init a new MyBatis project
    - https://www.youtube.com/watch?v=MrvOMJbA_B8&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=19

- SQL session
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/sqlsession.png">

- log4j logging
    - priority : FATAL > ERROR > WARN > INFO > DEBUG
    - log info becomes more detail from left to right (FATAL -> ... -> DEBUG)

- Mapping
    - Deal with java bean attr - SQL col mismatch case
     - solution 1) : use alias in SQL query in mapper xml
     - solution 2) : via mybatis conf (`mybatis.configuration.map-underscore-to-camel-case = true`)
     - solution 3) : via resultMap : use user-own defined mapping
    - multiple to one
        - solution 1) : resultMap, use multi-one mapping
        - solution 2) : association (plz check EmpMapper.xml)
        - solution 3) : association (steps SQL, 分步查詢) (used most often)
    - one to multiple
        - solution 1) : collection
        - solution 2) : collection (steps SQL, 分步查詢)
    - Ref
        - [TestEmpMapper](https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/src/test/java/com/yen/springMybatisDemo1/mapper/TestEmpMapper.java)

- Mybatis cache
    - ONLY works on `Select` op
    - Level 1 (local cache)（第一級快取)
        - default enabled
        - `SqlSession` level
    - Level 2 (second level cache)（第二級快取)
        - `SqlSessionFactory` level
          - to multiple SqlSession
    - So if there are BOTH level 1, and level 2 cache
      - WILL use level 2 cache -> level 1 cache -> query DB directly
      - when SqlSession is closed, level 1 cache is then updated to level 2 cache
    - https://matthung0807.blogspot.com/2019/02/mybatislocal-cache.html
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/cache1.png">
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/cache2.png">
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/cache3.png">
- Mybatis paging
    - mybatis-config.yml
        - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springMybatisDemo1/doc/pic/mybatis_config_ordering.png">

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=PG1lABauiSc&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=1