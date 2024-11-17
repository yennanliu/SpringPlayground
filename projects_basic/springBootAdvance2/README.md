# Spring boot advance 2
> RabbitMQ


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

## Important concept

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
			- Tricks
				-  `AmqpAdmin`: Create & remove : Queue, Exchange, Binding (via code)
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/jms_amqp.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ1.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ2.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ3.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ4.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ5.png">
	- <img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance2/doc/pic/RabbitMQ6.png">


## Ref
- Course
	- Material
		- video
			- https://www.youtube.com/watch?v=5zosKtv3-HA&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku
