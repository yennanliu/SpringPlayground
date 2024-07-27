# webFluxPoc

## End points

- http://localhost:8888/swagger-ui/ (Swagger UI)

- http://localhost:8888/books (GET)
- http://localhost:8888/books/{id} (GET)
  - http://localhost:8888/books/1

- http://localhost:8888/books (POST)
```bash
curl -X POST http://localhost:8888/books \
-H "Content-Type: application/json" \
-d '{
    "id": "1",
    "title": "Spring WebFlux",
    "author": "John Doe"
}'
```

## Ref
- https://juejin.cn/post/7129076913951211557