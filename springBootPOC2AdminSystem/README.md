# springBootPOC2AdminSystem
> Admin system with Spring Boot

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
| `GET` | GET | login page | http://localhost:8888/ |account: xxx, pwd:123|
| `GET` | GET | main page | http://localhost:8888/main.html ||
| `GET` | GET | upload file | http://localhost:8888/form_layouts ||
| `GET` | GET | basic table | http://localhost:8888/basic_table ||
| `GET` | GET | custom servlet V1 | http://localhost:8888/my_servlet ||
| `GET` | GET | custom servlet V2 | http://localhost:8888/my, http://localhost:8888/my01 ||
| `GET` | GET | mysql | http://localhost:8888/sql ||
| `GET` | GET | druid  | http://localhost:8888/druid/login.html |account : admin, pass : 123|
| `GET` | GET | mybatis 1 | http://localhost:8888/acct ||
| `GET` | GET | mybatis 2 | http://localhost:8888/city?id=5 | get city with id
| `POST` | POST | mybatis 3 | http://localhost:8888/city?name=may&state=OSK&country=JP|create city with name, state, country via POST

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
- DispatchServlet : how does it work ?
    - Steps
        - container set `DispatchServlet` attr, and bind it to `WebMvcProperties`.
            - corresponding conf : spring.mvc
        - via `ServletRegistrationBean<DispatchServlet>`, DispatchServlet is equipped.
        - default mapping path : `/`
            - can modify above in application.properties
    - Ref
        - https://www.youtube.com/watch?v=b-YRFm9OtAo&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=58
- Web native component inject (Servlet, Filter, Listener)
    - Servlet
        - method 1) : Servlet API
            - `@ServletComponentScan("com.yen.springBootPOC2AdminSystem")`
                - indicate where we put native components
            - `@WebServlet(urlPatterns = "/my_servlet")`
                - response directly, not passed to Spring Interceptor
        - method 2) : RegistrationBean
            - example : `com.yen.springBootPOC2AdminSystem.servlet.MyRegistConfig`
    - Ref
        - https://www.youtube.com/watch?v=oi6ChwpC6rc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=57

- Common custom methods
    - update conf files
    - xxxxCustomizer
    - xxxConfiguration + `@Bean` changes
        - `@Bean` exchange : add more default components, view parser
    -  `web app : A config class + implement WebMvcConfigurer, and @Bean some components` -> then can custom web features
        ```java
        // java, WebMvcConfigurer
        @Configuration
        public class AdminWebConfig implements WebMvcConfigurer 
        // ...
        ```
    - `@EnableWebMvc + WebMvcConfigurer` -- @Bean
        - can TAKE ALL CONTROLLER on SpringMVC
        - all rules can be defined by developer
        - implement custom setting, and extensions
        - theory
            - `WebMvcAutoConfiguration` is default SpringMVC auto setting clas : static resources, welcome page...
            - `DelegatingWebMvcRegistration`
                - take all WebMvcConfigurer in system, install all of their settings.
            - auto config some low level components, RequestMappingHandlerMapping. All these components are received from container
            - `public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport`
            - if we want setting in WebMvcAutoConfiguration work, we need to make `@ConditionalMissingBean(WebMvcConfigurationSupport.class)` work
            - `@EnableWebMvc` DISABLE `WebMvcAutoConfiguration`
    - Ref
        - https://www.youtube.com/watch?v=UbGHT87dXtU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=59

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