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
```

</details>

## API

| API | Type               | Purpose | Example cmd | Comment|
| ----- |--------------------| ---- | ----- | ---- |
| http://localhost:7777/stock/deduct | lock test endpoint | | |



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
