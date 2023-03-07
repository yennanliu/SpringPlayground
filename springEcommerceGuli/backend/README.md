# Backend

- Java spring boot/cloud

## Steps


## Run

<details>
<summary>App</summary>

- Install Nacos via Docker
    - https://nacos.io/zh-cn/docs/quick-start-docker.html
    ```bash

    # option 1) clone for code from cloud (prefer)
    rm -fr nacos-docker
    git clone https://github.com/nacos-group/nacos-docker.git
    cd nacos-docker
    docker-compose -f example/standalone-mysql-8.yaml up

    # docker-compose up, down with same flag
    # https://stackoverflow.com/questions/48717646/docker-compose-down-with-a-non-default-yml-file-name
    docker-compose -f example/standalone-mysql-8.yaml down

    # NOTE!!! if errors, can't start Nacos, do clean mysql lock or check logs
    cd nacos-docker
    rm -fr example/mysql/
    rm -fr example/standalone-logs/



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
# useful docker cmd
docker rm -f $(docker ps -aq)
docker rmi -f $(docker images -q)
docker rm -f $(docker ps -a -q)
docker rmi -f $(docker images -q -a)
# clean docker cache : https://stackoverflow.com/questions/65405562/is-there-a-way-to-clean-docker-build-cache
docker builder prune
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


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Nacos registry center | GET | http://127.0.0.1:8848/nacos/ | account, pwd: nacos|

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| API gateway | GET | http://127.0.0.1:88/ | gateway for all services|
| API gateway | GET | http://127.0.0.1:88/hello?url=goog | test (to fix)|
| Product list (via gateway) | GET | http://localhost:88/api/product/category/list/tree  | gateway for product service|
| delete product (via gateway) | POST | http://localhost:88/api/product/category/delete  | gateway for product service |
| update product sorting (via gateway) | POST | http://localhost:88/api/product/category/update/sort  | gateway for product service|
| update product brand (via gateway) | POST |http://localhost:88/api/product/brand/update  | gateway for product service |
| query 3 layer product (via gateway) | GET |http://localhost:88/api/product/attrgroup/list/1?page=1&key=phone  | gateway for product service |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Admin backend | GET | http://localhost:8080/renren-fast/ | | run this backend first, then run Admin frontend
| login test | GET | http://localhost:8080/renren-fast/captcha.jpg | | 

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Renren java code generator | GET | http://localhost:80 | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Coupon list | GET | http://localhost:7000/coupon/coupon/list | |
| Coupon test | GET | http://localhost:7000/coupon/coupon/test | |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Product list | GET | http://localhost:10000/product/attrattrgrouprelation/list | |
| Product tree list | GET | http://localhost:10000/product/category/list/tree | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Member list | GET | http://localhost:8000/member/growthchangehistory/list | |
| Member feign client test | GET | http://localhost:8000/member/member/coupons  | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Order list | GET | http://localhost:9000/order/order/list | |


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Ware list | GET | http://localhost:11000/ware/purchase/list | |


## Important Concepts

<details>
<summary>Concepts</summary>

- Feign remote client
    - https://youtu.be/G1SNCTRcKdE?t=227
    - [code ref](https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/backend/EcommerceGuli/gulimall-member/src/main/java/com/yen/gulimall/member/GulimallMemberApplication.java#L12)
    - 1) install open-feign
    - 2) create an interface tells springCloud that this service needs feign call
        - under feign pkg
            - declare which service, which endpoint the interface method is calling to
    - 3) enable feign remote call (EnableFeignClients)

- How to use Nacos as general conf setting
    - https://youtu.be/NMSk_q8czyI?t=669
    - 1) add Nacos dep (pom.xml)
    - 2) create "bootstrap.properties" under resources
        - spring.application.name=gulimall-coupon
        - spring.cloud.nacos.config.server-addr=127.0.0.1:8848
    - 3) setup a conf in Nacos
        - example : gulimall-coupon.properties (as Data ID) (default name : <application-name>.properties)
    - 4) setup whatever setting (k-v) in gulimall-coupon.properties
    - 5) sync with gulimall-coupon.properties in real-time ?
        - go to controller (e.g. CouponController)
        - add below annotation:
            - @RefreshScope
            - @Value("${key-name}") (get val from conf)
    - 6) priority:
        - Nacos conf > java conf (e.g. : application.properties)

- API gateway
    - https://youtu.be/2cmKrJDswek?t=119
    - https://github.com/yennanliu/SpringPlayground/tree/main/springAdvance/springCloud1#important-concepts
    - Route
    - Predicate
    - Filter
    - process:
        - request coming, check via "Predicate" to see if the request fit "Route" rules, if validation passed, then the request will be filter with "Filter" and redirected based on "Route"
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/api_gateway.png"></p>
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/api_gateway2.png"></p>

- CROS (跨域請求)
    - https://youtu.be/VNP6inKmw5I?t=526
    - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/CORS.png"></p>
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/CORS2.png"></p>
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/CORS3.png"></p>

- Product - attr relation
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/3_layer.png"></p>
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/sku_spu.png"></p>

</details>

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
