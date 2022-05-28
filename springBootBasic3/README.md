# springBootBasic3
> Spring Boot POC project (with book)

## Run
```bash
# start mysql
brew services start mysql

# start redis
brew services start redis

# build
mvn package

# run
java -jar <built_jar>
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | test | http://localhost:8888/mapping ||
| `GET` | GET | test | http://localhost:8888/hi ||
| `GET` | GET | test | http://localhost:8888/hello ||
| `GET` | GET | test | http://localhost:8888/showJDBC ||
| `GET` | GET | test | http://localhost:8888/hello_ft1 ||
| `GET` | GET | Ajax test | http://localhost:8888/index_ajax.html ||
| `GET` | GET | Rest web test | http://localhost:8888/index_blog.html ||
| `GET` | GET | test | http://localhost:8888/myServlet ||
| `GET` | GET | h2 demo | http://localhost:8888/h2-console |can get DB url from intellJ console when app runs (search h2)|
| `GET` | GET | show all books | http://localhost:8888/book/list ||
| `GET` | GET | add book | http://localhost:8888/book/add ||
| `GET` | GET | add book | http://localhost:8888/book/query ||
| `GET` | GET | add book | http://localhost:8888/account/transfer ||


## Ref

- Course
	- Material
		- [book](https://www.tenlong.com.tw/products/9787302528197?list_name=srh&fbclid=IwAR0AtIH5_D2RdOTcvsv7qH2bwKJpZBMmU-OkplnAMko5O5kzccBfgZf_-oU)
		- source code
		- video