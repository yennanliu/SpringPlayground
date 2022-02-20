# spring-playground

## Structure
```
# https://www.linkedin.com/posts/eczachly_softwareengineering-activity-6891156612426317824-iBfm

1. DAO (aka data access objects)
These files manage the connection with your database. They manage the CRUD operations.

2. Routes
These files manage HTTP and the networking layer of your server. The only logic here should be, request, response, and error handling of requests and responses.

3. Services
These files encapsulate the business logic of your application so you can use them in other areas of your application.

4. Middlewares
Middlewares usually sit in front of your API's routes. They do things like, logging, rate-limiting, etc. They are really critical to include for building a resilient, secure API.

5. Components
Components are pieces of the frontend that you want to reuse in multiple places. Imagine you built a fancy form component that you want to share among multiple pages.

6. Pages
Pages are the last piece of the puzzle and they're essentially other frontend components that are an amalgamation of components and services.
```

## Ref
- Projects
	- https://github.com/spring-guides/gs-serving-web-content
	- https://github.com/spring-attic/spring-mvc-showcase
- Video
	- https://www.youtube.com/watch?v=pS5HrZuvXLc&list=PLmOn9nNkQxJE3V_Eev79ao-g3a6BplSQG&index=1
	- https://www.youtube.com/codejava
- Blog
	- https://www.codejava.net/frameworks/spring-boot/spring-boot-crud-example-with-spring-mvc-spring-data-jpa-thymeleaf-hibernate-mysql#Database
