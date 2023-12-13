# Spring Advance - Distribution Lock
> xxx yyy

## Dep

- Java 17
- JDK 17
- Pressure test
  - nginx
  - jmeter
- Mysql
- Redis
- MySQL
- Zookeeper
- Lua script

## Build

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

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Test |  | | |



## Important Concepts

- 3 cases make local JVM lock failed
  1) 多例模式 (Multiton Pattern)
  2) 事務性 (transaction)
  3) cluster deployment (集群部署)
  Ref
     - https://youtu.be/pD8bEeq9q_U?si=z5OyNhApRaj2ARLp&t=21
     - https://youtu.be/CDaWk2RIBL4?si=D_FR2JKduE3JtCer&t=18

## Ref

- Course
    - Video
        - xxx
