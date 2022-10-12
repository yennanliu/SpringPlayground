# springSSO
> Build SSO system via spring boot

## Steps


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
| Swagger |  |  |  ||
| GET | GET | API page | http://localhost:8888/swagger-ui.html |swagger page|

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| GET |  GET | Test | http://localhost:8888/index/hello| |



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
  - https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver
  - https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver

- Book
    - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
        - ch.3
