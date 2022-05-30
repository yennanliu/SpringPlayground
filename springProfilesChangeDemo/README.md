# springProfilesChangeDemo
> Spring app load different profile (env) demo

## Run
```bash
# build
mvn package

# run
java -jar <built_jar>

# run with env profile
java -jar target/springProfilesChangeDemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# run with prod profile
java -jar target/springProfilesChangeDemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Monitor
```bash
jconsole
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | | http://localhost:8888/ ||
