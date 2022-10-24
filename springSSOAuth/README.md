# springSSOAuth

> Build SSO system via spring boot

<img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springSSOAuth/doc/pic/springSSOAuth_consul.png">

## Applications

- [springSSO](https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth/springSSO)
- [resourceServer](https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth/resourceServer)
- [gateway](https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth/gateway)

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
cd springSSOAuth
mkdir -p /tmp/consul/{conf,data}

docker run --name consel -p 8500:8500 -v /tmp/consul/conf/:/consul/conf/ -v /tmp/consul/data:/tmp/consul/data -d consul

docker ps -a

# access consul UI :
# http://localhost:8500/ui/dc1/services
```

```bash
#---------------------------
# Generate : security key
#---------------------------

# book p.3-21
keytool -genkeypair -alias mytest -keyalg RSA -keypass mypass -keystore keystore.jks -storepass mypass

# then copy keystore.jks to src/main/resource
cp keystore.jks springSSOAuth/springSSO/src/main/resources

# demo
# keytool -genkeypair -alias mytest -keyalg RSA -keypass mypass -keystore keystore.jks
# (base) ➜  springSSOAuth git:(main) ✗ keytool -genkeypair -alias mytest -keyalg RSA -k
# eypass mypass -keystore keystore.jks -storepass mypass
# What is your first and last name?
#   [Unknown]:  yen
# What is the name of your organizational unit?
#   [Unknown]:  yen
# What is the name of your organization?
#   [Unknown]:  yen
# What is the name of your City or Locality?
#   [Unknown]:  tokyo
# What is the name of your State or Province?
#   [Unknown]:  jp
# What is the two-letter country code for this unit?
#   [Unknown]:  jp
# Is CN=yen, OU=yen, O=yen, L=tokyo, ST=jp, C=jp correct?
#   [no]:  Y

# Generating 2,048 bit RSA key pair and self-signed certificate (SHA256withRSA) with a validity of 90 days
#   for: CN=yen, OU=yen, O=yen, L=tokyo, ST=jp, C=jp
# (base) ➜  springSSOAuth git:(main) ✗
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


</details>

## Important Concepts

- OAuth 2.0
  - roles
    - Resource owner
    - Resource server
    - Client
    - Auth server
  - mode
    - Auth code
    - Implicit code
    - Resource owner password credentials
    - Client credentials

## Ref

- Code
  - [chapter03-gateway/sso-authserver](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver)
  - [chapter03-gateway/sso-resourceserver](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver)
  - [chapter03-gateway/gateway](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-gateway)

- Book
    - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
        - ch.3
          - code
            - chapter03-sso-authserver/
            - chapter03-sso-resourceserver/