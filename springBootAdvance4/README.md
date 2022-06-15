# springBootAdvance4
> ZK, Euruka, demo

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

<details>
<summary>Zookeeper (Docker)</summary>

```bash
#---------------------------
# Run RabbitMQ
#---------------------------

# Install RabbitMQ (Docker)
# https://www.youtube.com/watch?v=IVjsiu0OrfQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=16
# management : has UI
docker pull rabbitmq:3.10-management

# check pull images
# account : guest, pwd: guest
docker images

# run Redis (Docker)
# -d : run in background
# -p : expose docker internal 6379 port to local machine's 6379 port 
# --name : name docker instance
# name of iamge we want to run
# 5672 : client, RabbitMQ port, 15672: UI port
docker run -d -p 5672:5672 -p 15672:15672 --name myrabbitmq <docker_img_id>

# visit RabbitMQ UI
# http://localhost:15672/

docker ps -a

# remove/stop container
docker stop <container_id>
docker rm  <container_id>
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | | http://localhost:8888/hello  ||


## Important Concepts


## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=wg8ZARFcCFk&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33