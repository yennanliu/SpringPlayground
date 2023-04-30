# markdown-blog
> Build a blog via spring boot with markdown
- Login:
  - account : admin
  - pwd : 123

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
| API      | Type | Purpose                         | Example cmd                                            | Comment                        |
|----------|------|---------------------------------|--------------------------------------------------------|--------------------------------|
| `GET`  | GET  | all posts                       | http://localhost:8888/posts/all                        | home page                      |
| `GET`  | GET  | all posts (with page)           | http://localhost:8888/posts/all?pageNum=1              | home page (with page)          |
| `GET`  | GET  | homepage  (with page, pageSize) | http://localhost:8888/posts/all?pageNum=0&pageSize=100 | home page (with page, size)    |
| `GET`  | GET  | login                           | http://localhost:8888/login                            | login (accout:admin, pwd: 123) |
| `POST` | POST | new post                        | http://localhost:8888/posts/create                     | create new post                |
| `GET`  | POST | edit post                       | http://localhost:8888/posts/edit/pre_edit              | edit post                      |
| `GET`  | GET  | show/edit author                | http://localhost:8888/author/all                       | show/edit author               |
| `GET`  | GET  | logout                          | http://localhost:8888/logout                           | logout                         |

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET /` | GET |Swagger | 	http://localhost:8888/swagger-ui.html |Api page|


## System dep
- Java 11 (JDK 11)


## TODO

<details>
<summary>TODO</summary>

1. paging
2. admin page (modify blogs ...)
3. filter blog
4. timeline
5. fix load history post
6. logout
7. 404, 500 ... html

</details>

## Reference
Markdown blog from below post series
- [Build a Markdown-based Blog with Spring Boot - Part 1](https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-1.html)
- [Build a Markdown-based Blog with Spring Boot - Part 2](https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-2.html)
- [Build a Markdown-based Blog with Spring Boot - Part 3](https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-3.html)
- [Build a Markdown-based Blog with Spring Boot - Part 4](https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-4.html)
- [Build a Markdown-based Blog with Spring Boot - Part 5](https://www.roshanadhikary.com.np/2021/07/build-a-markdown-based-blog-with-spring-boot-part-5.html)
- [Build a Markdown-based Blog with Spring Boot - Part 6](https://www.roshanadhikary.com.np/2021/07/build-a-markdown-based-blog-with-spring-boot-part-6.html)
- https://github.com/osopromadze/Spring-Boot-Blog-REST-API

## TODO
- admin page
  - edit post
- register page
  - integrate with github, google ..
- search page
  - search posts from user
