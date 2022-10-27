# Spring Course System


## Steps


## Run

<details>
<summary>App</summary>

- Run ddl first

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Login | http://localhost:8888/ssm | | |
| Login | http://localhost:8888 | | |
| Register | http://localhost:8888/security/register | | |
| Course list | http://localhost:8888/course/list | | |
| Input new course | http://localhost:8888/course/toInput | | |


## Important Concepts

## Ref

- Course
    - book
        - `Spring boot 開發實戰`
            - p,240
    - code
      - src
        - https://github.com/xiaoze-smirk/easy-springboot
      - mirror
        - https://github.com/yennanliu/SpringPlayground/tree/main/ref_project/easy-springboot-master