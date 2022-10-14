# springSSOAuth
> Build SSO system via spring boot

## Applications

- [springSSO](https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth/springSSO)
- [resourceServer](https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth/resourceServer)

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
          - code
            - chapter03-sso-authserver/
            - chapter03-sso-resourceserver/