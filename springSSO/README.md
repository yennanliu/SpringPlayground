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

- Book
    - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
        - ch.3
