# Download Service V2
> data download POC V2

## Steps


## User flows
```
Report type (report field) -> Create task (filter, feilds) -> Run task -> Notify when Task completed -> Download url -> History tasks
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
```

</details>


## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Download | | | |
| get report fields | GET | | http://localhost:8888/report/{name}, http://localhost:8888/report?name=trade , http://localhost:8888/report?name=deposit |
| test paging |GET | http://localhost:8888/msg?page=1&size=1 or http://localhost:8888/msg?page=1&size=2  or http://localhost:8888/msg?page=0&size=10| |
|  |  | | |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Test |  | | |
| test endpoint |GET | http://localhost:8888/test | |
| test endpoint |GET | http://localhost:8888/user | |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Swagger |  |  |  ||
| GET | GET | API page | http://localhost:8888/swagger-ui.html |swagger page|


## Important Concepts
- How to use local modules ?
    - step 1) add module to pom.xml
        - https://github.com/yennanliu/SpringPlayground/blob/main/DownloadServiceV2/download-service-api/pom.xml#L21
        - https://github.com/yennanliu/SpringPlayground/blob/main/DownloadServiceV2/download-service-web/pom.xml#L22
    - step 2) at intellJ, reload pom.xml (click "wheel")
    - step 3) then you should be able to import class from local modules
- Send `Map<String, Object>` with POST request via Postname ?
    - step 1) set up url (endpoint), choose POST type
    - step 2) Body -> raw -> choose "Json" type
    - step 3) add content into body (Map<String, Object>)
    - step 4) send request
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/DownloadServiceV2/doc/img/post_with_map_1.png">
    - <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/DownloadServiceV2/doc/img/post_with_map_2.png">
    - Ref
        - http://www.codebaoku.com/it-java/it-java-220952.html

## Ref