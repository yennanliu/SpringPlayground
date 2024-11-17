# springMultiThread
> Spring demo project with multi thread


## Steps


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
```

</details>

## API

| API | Type | Purpose | Example cmd                           | Comment|
| ----- | -------- | ---- |---------------------------------------| ---- |
| Swagger |  |  |                                       ||
| GET | GET | API page | http://localhost:8080/swagger-ui.html |swagger page|


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| POST | POST | upload multiple cars.csv with multi thread | http://localhost:8080/api/v1/car ||
| GET | GET | get all cars | http://localhost:8080/api/v1/car ||

## Important Concepts

## Ref

- Course
- Ref code
    - https://www.codeusingjava.com/boot/multi
    - https://github.com/swathisprasad/spring-boot-completable-future
        - https://swathisprasad.medium.com/multi-threading-in-spring-boot-using-completablefuture-a7ca68a0fe48
