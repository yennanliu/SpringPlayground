# springUserSystem
> Build user admin system via spring boot

## Steps


## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Install
#---------------------------

# Redis
# V1 (homebrew)
brew services start redis
redis-cli

# V2 (docker)
docker run -p 6379:6379 -v $PWD/data:/data -d redis:2.3 -server -appendonly yes
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



## Important Concepts

## Ref

- Book
    - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
        - ch.2
