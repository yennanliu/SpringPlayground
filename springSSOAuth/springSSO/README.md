# springSSO
> Build SSO system via spring boot

## Steps


## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Install : Consul
#---------------------------

# book p.2-31
# Consul
# V1 (docker)
cd springUserSystem
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
| GET | GET | API page | http://localhost:8888/swagger-ui.html |swagger page|

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| GET |  GET | Consul (service registry) | http://localhost:8500/ui/dc1/services| |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| GET |  GET | Test | http://localhost:8888/index/hello| |



## Ref
- Code
  - https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver

- Book
    - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
        - ch.3
