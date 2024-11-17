# springProfilesChangeDemo
> Spring app load different profile (env) demo

## Run
```bash
# build
mvn package

# run with dev profile
java -jar target/springProfilesChangeDemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# run with prod profile
java -jar target/springProfilesChangeDemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# run with dev profile with different name
java -jar target/springProfilesChangeDemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --person.name=RYU
```

## Monitor
```bash
jconsole
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | defaul env | | http://localhost:8888/ ||
| `GET` | dev env | | http://localhost:8887/ ||
| `GET` | prod env | | http://localhost:8886/ ||


## Important concept
- Externalized Configuration
	- Ref
		- [Externalized Configuration 1.0.1.RELEASE](https://docs.spring.io/spring-boot/docs/1.0.1.RELEASE/reference/html/boot-features-external-config.html)
		- [Externalized Configuration 2.7.0.RELEASE](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config)
	- Order (high -> load)
		- Command line arguments.
		- Java System properties (System.getProperties()).
		- OS environment variables.
		- @PropertySource annotations on your @Configuration classes.
		- Application properties outside of your packaged jar (application.properties including YAML and - profile variants).
		- Application properties packaged inside your jar (application.properties including YAML and profile variants).
		- Default properties (specified using SpringApplication.setDefaultProperties).
