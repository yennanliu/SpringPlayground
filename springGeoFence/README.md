# Spring Geo Fence

> Geo fence app with spring boot

- Tech
    - Spring boot
    - PostgreSQL
    - PostGIS
    - MapStruct

## Apps

- [efence](https://github.com/yennanliu/SpringPlayground/tree/main/springGeoFence/efence) : main app
- [configserver](https://github.com/yennanliu/SpringPlayground/tree/main/springGeoFence/configserver) : config server

## Steps


## Run

<details>
<summary>App</summary>


```bash
#------------------------------------------------------
# Install PostgreSQL (PostgreSQL 10.0 + PostGIS 2.4)
#------------------------------------------------------

# V1 : via Docker
# book p. 4-8
docker pull kartoza/postgis:10.0-2.4

docker run --name postgres1 -e POSTGRES_USER=gis -e POSTGRES_PASSWORD=123456 -p 54321:5432 -d kartoza/postgis:10.0-2.4

psql  -h 127.0.0.1 -p 54321 -d postgres -U gis -W 123456
# pwd : 123456

# psql useful cmd:
# https://github.com/yennanliu/utility_shell/blob/master/postgre/psql_command.sh

\l

\c gis

\d

# then execute ddl below
# https://github.com/yennanliu/SpringPlayground/blob/main/springGeoFence/efence/sql/ddl.sql
```


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
| Swagger |  |  |  ||
| GET | GET | API page | http://localhost:9090/swagger-ui.html |swagger 


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Fence API |  |  |  ||
| POST | POST | insert new layer | curl -X 'POST' \
  'http://localhost:9090/fence/layer/save' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "123",
  "name": "NYC",
  "businessType": 1,
  "cityCode": 1,
  "regionType": 1,
  "explain": "NYC CITY GEO POINT",
  "owner": "YEN"
}'|insert new layer


## Important Concepts

## Ref

- Course
    - code
        - [chapter04-efence](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence)
        - [chapter04-configserver](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-configserver)