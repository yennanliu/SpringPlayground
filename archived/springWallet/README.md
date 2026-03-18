# SpringWallet

> Build `wallet` service via spring boot

<img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springWallet/doc/pic/wallet_swagger.png">

<img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springWallet/doc/pic/wallet_hystrix.png">

- Frontend
- Backend

## Steps

- Step 1) Please install, run Consul first
- Step 2) Create DB, run all DDL
- Step 3) run app (backend)

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Install : Consul
#---------------------------

# https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth
# book p.2-31
# Consul
# V1 (docker)
cd springSSOAuth
mkdir -p /tmp/consul/{conf,data}

docker run --name consel -p 8500:8500 -v /tmp/consul/conf/:/consul/conf/ -v /tmp/consul/data:/tmp/consul/data -d consul

docker ps -a

# access consul UI :
# http://localhost:8500/ui/dc1/services
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
| Swagger |  |  |  ||
| GET | GET | API page | http://localhost:9090/swagger-ui.html |swagger 

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Hystrix Dashboard | http://localhost:9090/hystrix |service monitor |use `http://localhost:9090/hystrix.stream ` in stream name|
| Consul Dashboard | http://localhost:8500/ui/dc1/services |service monitor | |


## Important Concepts

## Ref

- Course
    - code
        - [chapter05-wallet](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet)
        - [chapter05-wallet-ui](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet-ui)
    - Book
        - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
            - ch.5
