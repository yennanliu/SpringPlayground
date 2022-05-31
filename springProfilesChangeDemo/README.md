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
