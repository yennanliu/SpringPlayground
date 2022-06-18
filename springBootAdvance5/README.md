# springBootAdvance5
>   SpringCloud Eureka integration demo

## Run
```bash
#---------------------
# Command line
#---------------------

# build
mvn package

# run
java -jar <built_jar>

#---------------------
# intellJ
#---------------------

# compile module
# maven -> click module -> package
# compiled jar can be found in "target"
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | | http://localhost:8761/ | Eureka UI
| `GET` | GET | | localhost:8001/ticket | get ticket endpoint

## Important Concepts

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=P5o-6Od5cfc&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34