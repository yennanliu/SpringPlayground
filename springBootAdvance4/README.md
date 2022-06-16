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

# https://www.youtube.com/watch?v=wg8ZARFcCFk&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33
docker pull zookeeper

# check pull images
# account : guest, pwd: guest
docker images

# run zookeeper (Docker)
# https://hub.docker.com/_/zookeeper
docker run --name zk01 -p 2181:2181 --restart always -d <zookeeper_img_id>

docker ps -a

# remove/stop container
docker stop <container_id>
docker rm  <container_id>
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | | http://localhost:8888/hello  ||


## Important Concepts


## Ref

- Course
    - Video
        - https://www.youtube.com/watch?v=wg8ZARFcCFk&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=33