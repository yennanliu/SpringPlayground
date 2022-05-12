# springBootPOC2AdminSystem
> Admin system with Spring Boot

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | login page | http://localhost:8888/ |account: xxx, pwd:123|
| `GET` | GET | main page | http://localhost:8888/main.html ||

## Important Concepts

- Interceptor
    <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootPOC2AdminSystem/doc/pic/interceptor1.png">
    - Steps
        - 1. Based on reqeust, find `HandlerExecutionChain`(handlers, and interceptors which can handle current request).
        - 2. `Normal order` run interceptors' `preHandle` method
            - If true, run next interceptors' preHandle
            - If false, `inverse order` run already-run interceptors' `afterCompletion` method
        - 3. If any interceptor run failed (return false), abort. Not run `target method`
        - 4. If all interceptors return true, run target method
        - 5. `Inverse order` run all interceptors' `postHandle` method
        - 6. If ANY of above failed, then TRIGGER afterCompletion method
        - 7. Page rendered success, then TRIGGER afterCompletion method
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