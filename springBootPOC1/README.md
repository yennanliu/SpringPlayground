# springBootPOC1
> Spring Boot POC project

## Quick start
- endpint : http://localhost:8888/hello


## Important concepts
- `spring-boot-starter-xxx-yyy` : quick starter that wraps and auto installs all depdency for a specigic dev scenario
	- https://www.javatpoint.com/spring-boot-starters
	- we can also create our own "starter"
- Location the Main Application class
	- files in main app's package or in main app's sub package CAN be scanned (visible to) by main app
	- if we really want app can scan files outside scope above, we can add below annotation to main app
		- `@SpringBootApplication(scanBasePackages = "com.yen")`
	<p align="center"><img src ="./doc/pic/main_app_hierarchy.png" ></p>
	- https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started.system-requirements

## Ref

<details>
<summary>Ref</summary>

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
			- https://github.com/yennanliu/springPlayground/tree/main/springboot2Course
- Project generator
	- https://start.spring.io/

</details>