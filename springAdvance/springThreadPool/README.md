# springThreadPool
> Spring demo project with thread pool, async op


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

| API | Type | Purpose              | Example cmd                 | Comment|
| ----- | -------- |----------------------|-----------------------------| ---- |
| Test | GET | hello world test     | http://localhost:8888/test  |
| Test | GET | multi thread test 1  | http://localhost:8888/test2 |
| Test | GET | async service call 1 | http://localhost:8888/test3 |

## Important Concepts

## Ref

- Course
    - Video
        - https://youtu.be/c134eGL062g?t=1603
        - https://youtu.be/c134eGL062g?t=2323
- Ref code
    - https://www.codeusingjava.com/boot/multi
    - https://github.com/swathisprasad/spring-boot-completable-future
        - https://swathisprasad.medium.com/multi-threading-in-spring-boot-using-completablefuture-a7ca68a0fe48
