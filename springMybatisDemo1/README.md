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
| get users by page | POST | test | `curl -X 'POST' \
  'http://localhost:8888/user/get_user?page=1&size=4' \
  -H 'accept: */*' \
  -d ''` |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Swagger UI | GET | test | http://localhost:8888/swagger-ui/index.html |

## Important Concepts

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=PG1lABauiSc&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=1