# Spring Geo Fence

> Geo fence app with spring boot

- Tech
    - Spring boot
    - PostgreSQL


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
| Test |  | | |



## Important Concepts

## Ref

- Course
    - code
        - [chapter04-efence](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence)
        - [chapter04-configserver](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-configserver)