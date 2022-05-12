# springBootPOC2AdminSystem
> Backend admin system with Spring Boot

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | login page | http://localhost:8888/ |account: xxx, pwd:123|
| `GET` | GET | main page | http://localhost:8888/main.html ||

## Important Concepts

- Interceptor
    - Steps
        - Based on reqeust, find `HandlerExecutionChain`(handlers, and interceptors which can handle current request).
        - `normal order` run interceptors' `preHandle` method
            - if true, run next interceptors' preHandle
            - if false, `inverse order` run already-run interceptors' `afterCompletion` method
        - if any interceptor run failed (return false), abort. Not run `target method`
        - if all interceptors return true, run target method
        - `inverse order` run all interceptors' `postHandle` method
    - example
        `preHandle -> target method -> postHandle -> afterCompletion`
            - preHandle : before "target method"
            - postHandle : after "target method"
            - afterCompletion : after "page is render"
    - Ref
        - https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49
        - https://www.youtube.com/watch?v=wSMi4qkxnJo&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49

## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=xu33IJNxkn0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u
    - Doc material
        - https://www.yuque.com/atguigu/springboot/?fbclid=IwAR3FhDeXH7A73kH8PubSWgkVLmwmsCwR9nFKCn2dp0KalBY6mpBCnZ2eQxs
    - Source code
        - origin
            - https://gitee.com/leifengyang/springboot2
        - mirror
            - https://github.com/yennanliu/springPlayground/tree/main/courses/springboot2Course
- Project generator
    - https://start.spring.io/