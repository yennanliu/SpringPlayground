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
| cloud-provider-payment8001 |  | | |
| get payment | GET | http://localhost:8001/payment/get/1 | get payment by id (via payment-8001)|
| crearte payment | POST | http://localhost:8001/payment/
create?serial=549494489 | create new payment with `Payment` (serial)|

| API | Type | Example cmd | Purpose | Comment|
| ----- | -------- | ---- | ----- | ---- |
| cloud-consumer-order80 |  | | |
| get payment | GET | http://localhost/consumer/payment/get/4| get payment by id (via consumer-order-80)|
| crearte payment | GET | http://localhost:80/consumer/payment/create?serial=7777 | create new payment with `Payment` (serial) via consumer-order-80)|

| API | Type | Example cmd | Purpose | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Eureka |  | | |
| service UI | GET | http://localhost:7001/ | Eureka UI | Note : can access payment8001, order80 services via eureka UI as well

| API | Type | Example cmd | Purpose | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Test |  | | |
| test1 | GET | http://localhost:8001/test | |


## Important Concepts
- RestTemplate
    - via `restTemplate` visit Rest endpoints
    - params:
        - url : REST address
        - requestMap : request params
        - ResponseBean.class : HTTP response encapsulation class
    - Ref
         - https://youtu.be/8d6BvCZxPwQ?t=604
         - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
         - https://www.baeldung.com/rest-template
- Project refactor (extract common methods to module, and use it)
    - https://www.youtube.com/watch?v=D1pH2Ee88OM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=15
- Eureka
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springCloud1/doc/pic/eureka1.png">

## Ref
- Course
    - Video
        - https://www.youtube.com/watch?v=MK5KDjTQysA&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0