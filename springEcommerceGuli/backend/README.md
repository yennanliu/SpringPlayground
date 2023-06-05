# Backend

- Java spring boot/cloud

## Steps


## Run

<details>
<summary>App</summary>

- Install Nacos via Docker
    - https://nacos.io/zh-cn/docs/quick-start-docker.html
    ```bash

    # Option 1) use M1 docker image
    # https://hub.docker.com/r/zhusaidong/nacos-server-m1
    # chttps://github.com/alibaba/nacos/issues/6340
    docker pull zhusaidong/nacos-server-m1:2.0.3
    docker run --name nacos-standalone -e MODE=standalone -e JVM_XMS=512m -e JVM_XMX=512m -e JVM_XMN=256m -p 8848:8848 -d zhusaidong/nacos-server-m1:2.0.3

    # Option 2) clone for code from cloud (prefer)
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

    # # option 3) use local code snapshot
    # cd springEcommerceGuli/backend/nacos-docker-master
    # # mysql-8 stand alone
    # docker-compose -f example/standalone-mysql-8.yaml up
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

- Install ES, Kibana via Docker
```bash
# pull image
# https://youtu.be/uhuoHo3PYR8?t=72
# setup conf
# mkdir -p mydata/elasticsearch/config
# mkdir -p mydata/elasticsearch/data
# echo "http:host: 0.0.0.0" >> "mydata/elasticsearch/config/elasticsearch.yml"
# chmod -R 777  mydata/elasticsearch

# run ES
# https://stackoverflow.com/questions/65962810/m1-mac-issue-bringing-up-elasticsearch-cannot-run-jdk-bin-java
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.10.2

# https://myapollo.com.tw/blog/docker-elasticsearch/
# https://blog.51cto.com/wangzhenjun/4974913
#docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node"  -e ES_JAVA_OPS="-Xms64m -Xmx128m" -e "xpack.security.enabled=false" -e "http.host: 0.0.0.0"  -e "network.host: 127.0.0.1" --name= elasticsearch:8.7.0

# run kibana
#docker run -p 5601:5601 -e ELASTICSEARCH_HOSTS=http://localhost:9200/ --name= kibana:8.7.0
```
- Note :
    - there is issue run ES and kibana together on Macbook M1, so here I only run ES 7.10.2 docker and use it via REST API (postman)
        - [ES REST cmd](https://github.com/yennanliu/utility_shell/blob/master/elk/elasticsearch/REST_command.sh)
- http://localhost:9200/
- http://localhost:5601/


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
    - Note:
        - If Json model is compatible, feign service, and feign client CAN use different To (data transfer object)
        - https://youtu.be/2Fgtxnc9ehQ?t=848

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
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/CORS_workaround_1.png"></p>
<p align="center"><img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/doc/pic/CORS_workaround_2.png"></p>

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
        - https://s8jl-my.sharepoint.com/personal/atguigu_s8jl_onmicrosoft_com/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fatguigu%5Fs8jl%5Fonmicrosoft%5Fcom%2FDocuments%2F%E5%B0%9A%E7%A1%85%E8%B0%B7%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E%E7%94%B5%E5%95%86%E9%A1%B9%E7%9B%AE&ga=1
        - DML, DDL
            - https://github.com/yennanliu/SpringPlayground/tree/main/courses/%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E_%E5%85%A8%E6%A3%A7%E9%96%8B%E7%99%BC_src_code/docs/%E4%BB%A3%E7%A0%81/sql
    - renren fast code:
        - https://gitee.com/renrenio
    - renren-generator (Reverse engineering)
        - https://gitee.com/renrenio/renren-generator/blob/master/pom.xml
