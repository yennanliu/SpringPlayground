# Spring boot advance 1
> Cache (Redis)


## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>


#---------------------------
# Run Mysql
#---------------------------
brew services start mysql
mysql -u root
```

</details>


<details>
<summary>Redis (Docker)</summary>

```bash
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

# remove/stop container
docker stop <container_id>
docker rm  <container_id>

# basic command
# https://redis.io/commands/

# 1) String
# append to key (msg)
append msg hello
append msg world

# get by key
get msg

# 2) List
# push into list
LPUSH mylist 1 2 3

# pop element from list (from left, right)
LPOP mylist
RPOP mylist

# 3) Set
SADD myset tim jack

SMEMBERS myset

# 4) check is elment in a key
SISMEMBER myset lynn
SISMEMBER myset tim
```
</details>


<details>
<summary>RabbitMQ (Docker)</summary>

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
</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | get employee by id | http://localhost:8888/emp/{id}, http://localhost:8888/emp/1|id=1 or 2 or 3...|
| `POST` | POST | update employee by id | http://localhost:8888/emp |id=xxx, lastName=yyy ...|
| `GET` | GET | delete employee by id | http://localhost:8888/delemp?id=1 |id=1 or 2 or 3...|
| `GET` | GET | get employee by lastName | http://localhost:8888/lastname/{lastname}, http://localhost:8888/emp/lastname/Jill |lastname=Bob or Lily or Kim...|
| `GET` | GET | get dept by id | http://localhost:8888/dept/{id}, http://localhost:8888/dept/1|id=1 or 2 or 3...|

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

- Message (and spring boot)
	- form
		- queue
		- topic (pub-sub)
	- method
		- JMS
			- Java Message Service, implementation based on JVM
			- ActiveMQ, HornetMQ
		- AMQP
			- Advanced Message Queuing Protocol
			- RabbitMQ is one of the implementations
		- RabbitMQ
			- AMQP implementation via erlang
			- Componenets
				- Message
					- rounting-key
					- priority
					- delivery-mode
				- Publisher
				- Exchange
					- direct
					- fanout topic
					- headers
				- Queue
				- Binding
				- Connection
				- Channel
				- Consumer
				- Virtual Host
				- Broker
			- Exchange type
				- direct exchange
				- fanout exchange
				- topic exchange
			- Basic RabbitMQ demo
				- https://www.youtube.com/watch?v=IVjsiu0OrfQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=16
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/jms_amqp.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ1.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ2.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ3.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ4.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ5.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/RabbitMQ6.png">


## Ref
- Course
	- Material
		- video
			- https://www.youtube.com/watch?v=5zosKtv3-HA&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku
