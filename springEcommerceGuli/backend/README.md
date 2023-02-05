# Backend

- Java spring boot/cloud

## Steps


## Run

<details>
<summary>App</summary>

- Install Nacos via Docker
    - https://nacos.io/zh-cn/docs/quick-start-docker.html
    ```bash

    # option 1) clone for code from cloud
    git clone https://github.com/nacos-group/nacos-docker.git
    cd nacos-docker

    # option 2) use local code snapshot
    cd springEcommerceGuli/backend/nacos-docker-master

    # mysql-8 stand alone
    docker-compose -f example/standalone-mysql-8.yaml up
    ```
    - access (default)
        - accout: nacos
        - pwd: nacos
    - Note: There is an issue running Nacos via compiled code in Macbook M1

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


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Nacos registry center | GET | http://127.0.0.1:8848/nacos/ | account, pwd: nacos|


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Admin backend | GET | http://localhost:8080/renren-fast/ | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Renren java code generator | GET | http://localhost:80 | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Coupon list | GET | http://localhost:7000/coupon/coupon/list | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Product list | GET | http://localhost:10000/product/attrattrgrouprelation/list | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Member list | GET | http://localhost:8000/member/growthchangehistory/list | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Order list | GET | http://localhost:9000/order/order/list | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Ware list | GET | http://localhost:11000/ware/purchase/list | |


## Important Concepts

## Ref

- Course
    - Video
        - https://www.youtube.com/playlist?list=PLmOn9nNkQxJEwPjhNwGliP_bw3RjkgFCf
- Ref code
    - backend:
        - https://github.com/yennanliu/SpringPlayground/tree/main/courses/%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E_%E5%85%A8%E6%A3%A7%E9%96%8B%E7%99%BC_src_code/docs/%E4%BB%A3%E7%A0%81/gulimall
    - renren fast code:
        - https://gitee.com/renrenio
    - renren-generator (Reverse engineering)
        - https://gitee.com/renrenio/renren-generator/blob/master/pom.xml
