# Spring Cloud 1
> Spring cloud micro-service demo
<img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springCloud1/doc/pic/spring_cloud_intro.png">

## Steps
```
Create module -> modify POM -> create YML -> main app -> biz class
```

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>


#---------------------------
# Run Mysql
#---------------------------
brew services start mysql
mysql -u root
```

</details>

## API

| API | Type | Example cmd | Purpose | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Payment |  | | |
| get payment | GET | http://localhost:8001/payment/get/1 | get payment by id |
| crearte payment | POST | http://localhost:8001/payment/create?serial=549494489 | create new payment with `Payment` (serial)|
| Test |  | | |
| test1 | GET | http://localhost:8001/test | |


## Important Concepts

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=MK5KDjTQysA&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0
