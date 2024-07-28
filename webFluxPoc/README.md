# webFluxPoc

## End points

- http://localhost:8888/books (GET)
- http://localhost:8888/books/{id} (GET)
  - http://localhost:8888/books/1

- http://localhost:8888/books (POST)
```bash
curl -X POST http://localhost:8888/books \
-H "Content-Type: application/json" \
-d '{
    "id": "100",
    "name": "Spring WebFlux",
    "author": "John Doe"
}'
```

- http://localhost:8888/test/delay (GET)


## Ref
- https://juejin.cn/post/7129076913951211557
- https://youtube.com/playlist?list=PLmOn9nNkQxJGZOf_WLh7FYNKXg44qLPg7&si=3jrdt9lyq84p4vKe