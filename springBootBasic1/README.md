# springBootBasic1
> Spring Boot basic project

## Quick Start

```bash
#---------------------
# Maven
#---------------------

# build
mvn package

# run
java -jar <built_jar>

#---------------------
# intellJ
#---------------------
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET /` | GET | test endpoint |http://localhost:8888/ |home page|
| `GET /hello` | GET | test endpoint |http://localhost:8888/hello ||
| `GET /hello2?name=xxx` | GET | test endpoint |http://localhost:8888/hello2?name=<name> ||
| `GET /hello3` | GET | test endpoint |http://localhost:8888/hello3 ||
| `GET /hello4` | GET | test endpoint |http://localhost:8888/hello4||
| `GET /world` | GET | test endpoint |http://localhost:8888/world ||
| `GET /car` | GET | test endpoint |http://localhost:8888/car ||
| `GET /car2` | GET | test endpoint |http://localhost:8888/car2 ||
| `GET /person` | GET | test endpoint |http://localhost:8888/person ||
| `GET /goto` | GET | test endpoint |http://localhost:8888/goto ||
| `GET` | GET | test endpoint |http://localhost:8888/cars/sell;low=34;brand=LEXUS,porsche,audi||
| `GET` | GET | test endpoint | http://localhost:8888/boss/1;age=20/2;age=10  ||
| `GET /test/person2` | GET | test |http://localhost:8888/test/person2 ||
| `GET /test/person` | GET | test |http://localhost:8888/person?format=json, http://localhost:8888/person?format=xml ||
| `GET /test/file_resource` | GET | test |http://localhost:8888/test/file_resource ||
| `GET /yen_test1` | GET | test |http://localhost:8888/yen_test1 | thymeleaf test|

## Important Concepts

- `spring-boot-starter-xxx-yyy` : quick starter that wraps and auto installs all depdency for a specigic dev scenario
    - https://www.javatpoint.com/spring-boot-starters
    - we can also create our own "starter"
- Location the Main Application class
    - files in main app's package or in main app's sub package CAN be scanned (visible to) by main app
    - if we really want app can scan files outside scope above, we can add below annotation to main app
        - `@SpringBootApplication` or `@ComponentScan`
    <p align="center"><img src ="./doc/pic/main_app_hierarchy.png" ></p>
    - https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started.system-requirements
- Config binding
    - https://www.yuque.com/atguigu/springboot/qb7hy2
    - method 1)
        - `@Component + @ConfigurationProperties`
    - method 2)
        - `@EnableConfigurationProperties + @ConfigurationProperties`
            - for 3rd party lib
    - method 3)
        - `@Component + @ConfigurationProperties`
- Auto config
    - https://youtu.be/lDzXRsOODXA?t=1708
    - Spring Boot auto import `all auto config classes` : `xxxAutoConfiguration`
    - Every auto config class is enabled with conditions
        - default : get value from config
        - from `xxxProperties`, binding with it
    - Enabled config class will be added to container
    - Once above happened, container then has those functionality
    - Custom setting
        - users replace underline components via their own `"@Bean"`
        - users change setting via config file (e.g. `xxxProperties`)
    - `xxxAutoConfiguration` -> components -> `xxxProperties` get value -> application.properties
- Static resources
    - Binding config with xxx via :
        - WebMvcProperties == `spring.mvc`
        - ResourceProperties == `spring.resources`
- `REST` development
    - Form request (表單)
        - since form can send `GET`, `POST` request ONLY
        - Enable `REST` in Spring boot:
            - we need add below setting to config (application.yml or application.conf)
            ```ymal
            Spring:
              mvc:
                hiddenmethod:
                  filter:
                    enabled: true
            ```
        - form request NEED with `_method=put`
            ```ymal
            <form action="user" method="post">
            ```
        - Request will be parsed by `HiddenHttpMethodFilter`
            - check if request is correct, and is POST
            - get `_method` value
            - be compatible to below requests:
                - HttpMethod.PUT.name()
                - HttpMethod.DELETE.name()
                - HttpMethod.PATCH.name()
            - default requst : POST
                - Wrapper mode overwrides `getMethod()` method, so it returns input value 
                    - wrapper is used (in filter), when use `getMethod` is using `requestWrapper` actually
    - Client side app
        - postman, curl...
        - HiddenHttpMethodFilter is NOT needed
        - we can send POST, GET, PUT, DELETE.. directly
- Content Negotiation (內容協商)
    - Response page
    - Response data
        - `@ResponseBody` in java code
    - ref
        - https://www.youtube.com/watch?v=QBJAQOe4giA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=39
        - https://www.youtube.com/watch?v=eIrvgWThto0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=40
        - https://www.youtube.com/watch?v=NEGzyvm1IBc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=42
    - client gets server response in `different format` based on `request header`
    - browser (client) uses `header` (default) let server know : which content type is acceptable to it.
    - theory
        - check if current request header already has default val (MediaType)
        - get client (e.g. PostMan, web ...) support content type (client Accept info)
        - loop over current MessageConverter, check which one supports current request
        - get the "best match" (from above)
        - transform it via converter (above), and get needed type
    - example:
        - change `Accept` param in request header (Http protocol)
            - web : xml/json (depends on header)
            - mobile : json
    - note :
        - "Weights of request type"
            - e.g. : `applicatiion/json,q=0.8`, `applicatiion/xml,q=0.7` ...
    - User defined `MessageConverter`
        - step 1 : `@ResponseBody` in java code
        - step 2: Processor return method val, processed via MessageConverter
        - step 3: get all possible MessageConverter combinations
        - stpe 4: find the final MessageConverter
- thymeleaf
    - [offical doc](https://www.thymeleaf.org/)
    - ref
        - https://www.youtube.com/watch?v=MuzLKTp87Vs&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44
        - com.yen.SpringBootPart1.controller.ViewController.java
    - auto config strategy
        - All thymeleaf default val is in ThymeleafProperties
        - SpringTemplateEngine already auto config
        - ThymeleafViewResolver already auto config
        - The only thing developer needs to do : `develop html page`
        - Note below conf:
        ```java
        // java
        public static final String DEFAULT_PREFIX = "classpath:/templates/";  // 前綴
        public static final String DEFAULT_SUFFIX = ".html"; // 後綴
        ```

## Ref

- Init Spring boot project
    - https://www.youtube.com/watch?v=RreK80HHAAk&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=19
    - https://start.spring.io/
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
