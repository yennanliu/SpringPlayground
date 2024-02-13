# Spring Bank
> Build Bank via Spring boot

## Build

## Run (manually)

<details>  
<summary>App</summary>  

```bash  
#---------------------------  
# Run app  
#---------------------------  
  
# build  
mvn package  
  
# run  

# springBank/springBankApp/target/springBankApp-0.0.1-SNAPSHOT.jar

cd SpringPlayground/springBank/springBankApp

java -jar target/springBankApp-0.0.1-SNAPSHOT.jar --server.port=9999
java -jar target/springBankApp-0.0.1-SNAPSHOT.jar --server.port=9998
  
# cehck run services  
brew services list


#---------------------------  
# Run nginx  
#---------------------------  
  
# https://github.com/yennanliu/utility_shell/blob/master/nginx/install_nginx.sh  
  
# http://localhost:8080/  
  
# start  
brew services start nginx  
  
# stop  
brew services stop nginx  


# default config
# /usr/local/etc/nginx
# /usr/local/etc/nginx/nginx.conf 

# macbook M1
# /opt/homebrew/etc/nginx
# /opt/homebrew/etc/nginx/nginx.conf
  
#---------------------------  
# Run jmeter  
#---------------------------  
  
# https://youtu.be/-EeTUjNlkN0?si=llNkPSRd2j5hvvsl&t=108  
# https://github.com/yennanliu/utility_shell/blob/master/jmeter/install_%20jmeter.sh  
  
cd apache-jmeter-5.6.2  
bash bin/jmeter  
  
# reload config  
nginx -s reload 

#---------------------------  
# Run redis  
#--------------------------- 

brew services start redis
 
brew services stop redis


#---------------------------  
# Run zookeeper  
#--------------------------- 

# https://husterxsp.github.io/2018/10/08/zookeeper%E5%AE%89%E8%A3%85/
# https://github.com/yennanliu/utility_shell/blob/master/zookeeper/zk_cmd.sh

brew services run zookeeper

brew services stop zookeeper

# file path
# /usr/local/etc/zookeeper

cd /usr/local/etc/zookeeper

zkCli

#---------------------------  
# Intellij  
#---------------------------  
  
# https://github.com/yennanliu/utility_shell/blob/master/intellij/intellij_command.sh  
# 30) Allow run app in parallel (multiple instances)  
# https://intellij-support.jetbrains.com/hc/en-us/community/posts/360010505820-Why-my-2020-3-2-IntelliJ-IDEA-Allow-parallel-run-check-box-is-missing  
# configuration -> select main app name -> "Modify options"  -> click "allow multiple instances check box  
```  

</details> 


## Run (docker)

<details>  
<summary>App</summary>  

```bash
#---------------------------  
# Macbook Intel chip
#---------------------------  
docker-compose up

# restart
docker-compose restart

#---------------------------  
# Macbook apple M1 chip
#---------------------------  
docker-compose -f docker-compose-m1.yml up

# restart
docker-compose -f docker-compose-m1.yml restart
```  

</details> 


## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:9999/swagger-ui.html | API page (BE) | | |
| http://localhost:9999/balance/ | Balance List | | |
| http://localhost:8080/balance/ | Balance List (Nginx) | | |


## Ref

- code
	- source
		- https://github.com/ohbus/retail-banking
	- mirror
		- https://github.com/yennanliu/retail-banking

## TODO

- Endpoints
	- GET /user/<user_id> : use balance
	- POST /user/deduct/<user_id> : deduct use balance
	- POST /transfer/<user_id_1>/<user_id_2>/<amount> :  transfer amount of money from user_id_1 to user_id_2

- Requirement
	- Cluster deployment
	- Distributed lock implement (with Redis)