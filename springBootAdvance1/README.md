# Spring boot advance 1
> Spring boot advance demo


## Run
```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>



#---------------------------
# Run Redis
#---------------------------

# Install Redis (Docker)
# https://hub.docker.com/_/redis
# https://www.youtube.com/watch?v=c3lLy3KCjYk&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=10
docker pull redis

# check pull images
docker images

# run Redis (Docker)
# -d : run in background
# -p : expose docker internal 6379 port to local machine's 6379 port 
# --name : name docker instance
# name of iamge we want to run
docker run -d -p 6379:6379 --name myredis redis

docker ps -a

redis-cli
```

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | get employee by id | http://localhost:8888/emp/{id}, http://localhost:8888/emp/1|id=1 or 2 or 3...|
| `POST` | POST | update employee by id | http://localhost:8888/emp |id=xxx, lastName=yyy ...|
| `GET` | GET | delete employee by id | http://localhost:8888/delemp?id=1 |id=1 or 2 or 3...|
| `GET` | GET | get employee by lastName | http://localhost:8888/lastname/{lastname}, http://localhost:8888/emp/lastname/Jill |lastname=Bob or Lily or Kim...|

## Important concept

- Cache
	- since Spring 3.1
		- `org.springframwork.cache.Cache`
			- cache implementation, define cache op
				- class : RedisCache, EhCacheCache, ConcurrentMapCache..
		- `org.springframwork.cache.CacheManager`
			- cache manager, manage cache components
		- `@Cacheable`
			- if implement on a method, then such method will have cache mechanism
		- `@CacheEvict`
			- clean cache
		- `@CachePut`
			- make sure method is called, put result to cache
		- `@EnableCaching`
			- enable cache (based on annotation)
		- `@keyGenerator`
			- generate key for cache
		- `@serialize`
			- serialization for cache value
		- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/cache1.png">
		- ref
			- https://www.youtube.com/watch?v=puGukebF5E0&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3

## Ref
- Course
	- Material
		- video
			- https://www.youtube.com/watch?v=5zosKtv3-HA&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku
