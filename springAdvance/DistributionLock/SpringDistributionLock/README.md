# Spring Advance - Distribution Lock
> Distribution Lock demo with Spring boot application

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


#---------------------------
# Run nginx
#---------------------------

# https://github.com/yennanliu/utility_shell/blob/master/nginx/install_nginx.sh

# http://localhost:8080/

# start
brew services start nginx

# stop
brew services stop nginx

#---------------------------
# Run nginx
#---------------------------

# https://youtu.be/-EeTUjNlkN0?si=llNkPSRd2j5hvvsl&t=108
# https://github.com/yennanliu/utility_shell/blob/master/jmeter/install_%20jmeter.sh

cd apache-jmeter-5.6.2
bash bin/jmeter

# reload config
nginx -s reload

#---------------------------
# Intellij
#---------------------------

# https://github.com/yennanliu/utility_shell/blob/master/intellij/intellij_command.sh
# 30) Allow run app in parallel (multiple instances)
# https://intellij-support.jetbrains.com/hc/en-us/community/posts/360010505820-Why-my-2020-3-2-IntelliJ-IDEA-Allow-parallel-run-check-box-is-missing
# configuration -> select main app name -> "Modify options"  -> click "allow multiple instances check box
```

</details>

## API

| API | Type               | Purpose | Example cmd | Comment|
| ----- |--------------------| ---- | ----- | ---- |
| http://localhost:7777/stock/deduct | lock test endpoint | | |



## Important Concepts

- 3 cases make local JVM lock failed
  1) 多例模式 (Multiton Pattern)
     -[video](https://youtu.be/L7OFClDyWLs?si=_rYUlzOFkuDD8PRU&t=75)
  2) 事務性 (transaction)
     -[video](https://youtu.be/pD8bEeq9q_U?si=GQQ1rD-Wt0BalBwm)
     - ACID : atomicity, consistency, isolation, and durability
     - also relative to isolation, which is different in different DB
     - https://github.com/yennanliu/CS_basics/blob/master/doc/faq/db/db_isolation_levels.md
  3) cluster deployment (集群部署)
  Ref
     - https://youtu.be/L7OFClDyWLs?si=HNMvaI6WeAoJZ3lk
     - https://youtu.be/pD8bEeq9q_U?si=z5OyNhApRaj2ARLp&t=21
     - https://youtu.be/CDaWk2RIBL4?si=D_FR2JKduE3JtCer&t=18

## Ref

- Course
    - Video
        - xxx
