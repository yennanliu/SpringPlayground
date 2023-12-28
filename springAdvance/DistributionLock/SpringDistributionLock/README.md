
# Spring Advance - Distribution Lock
> Distribution Lock demo with Spring boot application

## Dep

- Java 17
- JDK 17
- Pressure test
    - nginx
    - jmeter
- Mysql
- Redis
- MySQL
- Zookeeper
- Lua script

## Build

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
# Run nginx  
#---------------------------  
  
# https://github.com/yennanliu/utility_shell/blob/master/nginx/install_nginx.sh  
  
# http://localhost:8080/  
  
# start  
brew services start nginx  
  
# stop  
brew services stop nginx  
  
#---------------------------  
# Run nginx  
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
# Intellij  
#---------------------------  
  
# https://github.com/yennanliu/utility_shell/blob/master/intellij/intellij_command.sh  
# 30) Allow run app in parallel (multiple instances)  
# https://intellij-support.jetbrains.com/hc/en-us/community/posts/360010505820-Why-my-2020-3-2-IntelliJ-IDEA-Allow-parallel-run-check-box-is-missing  
# configuration -> select main app name -> "Modify options"  -> click "allow multiple instances check box  
```  

</details>  

## API

| API                                | Type                             | Purpose | Example cmd | Comment|  
|------------------------------------|----------------------------------| ---- | ----- | ---- |  
| http://localhost:7777/stock/deduct | lock test endpoint               | | |
| http://localhost:8080/stock/deduct   | Nginx endpoint | | |


## Important Concepts

### 1) Lock comparison

- AVOID use JVM local lock (only work for single instance, singleton, since it CAN'T deal with data consistency outside instance (e.g. N instances, 1 MySQL)
- performance:
  - lock with single SQL > pessimistic lock > JVM lock > optimistic lock
- If for good performance, simple biz logic, and no need to record change history
  - use `lock with single SQL`
- If read > write
  - use `optimistic lock`, not heavy race condition
- If write > read, race condition happens often
  - use `pessimistic lock`

### 2) 3 cases make local JVM lock failed

- case 1) `多例模式 (Multiton Pattern)`
    - [video](https://youtu.be/L7OFClDyWLs?si=_rYUlzOFkuDD8PRU&t=75)

- case 2) `事務性 (transaction)`
    - [video](https://youtu.be/pD8bEeq9q_U?si=GQQ1rD-Wt0BalBwm)
    - ACID : atomicity, consistency, isolation, and durability  
      also relative to isolation, which is different in different DB
    -  https://github.com/yennanliu/CS_basics/blob/master/doc/faq/db/db_isolation_levels.md

- case 3) `cluster deployment (集群部署)`
    - Ref
        - https://youtu.be/L7OFClDyWLs?si=HNMvaI6WeAoJZ3lk
        - https://youtu.be/pD8bEeq9q_U?si=z5OyNhApRaj2ARLp&t=21
        - https://youtu.be/CDaWk2RIBL4?si=D_FR2JKduE3JtCer&t=18

### 3) 行級悲觀鎖

- 如果想要行級鎖(只鎖住選定資料) 必須同時滿足以下二個條件:
  - https://youtu.be/HyD7E8WkJhI?si=oTQgzl92MElMfjbX&t=38
  - 1) 鎖的查詢/更新條件必須是 index (索引)
  - 2) 查詢/更新條件必須是具體值 (example : 不可以是 模糊查詢, like
- 時間戳+版本號
- CAS algorithm
  - compare and swap - (if old version == new version, then update or abort)
- pros
- cons
  - low performance when multi thread (worse than pessimistic lock)
  - ABA issue (cause by CAS algorithm)
    - a record may change from A to B, then to A, but users may NOT be aware of it
  - optimistic lock may fail when write-read separation (讀寫分離, 主從模式)

### 4) Redis lock

- [Optimistic lock](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/service/StockServiceRedisOptimisticLock.java)
  - steps : watch -> multi -> exec
  - pros
  - cons
    - low performance
- Distribution lock
  - cases
    - over sales
    - Cache Breakdown (緩存擊穿)
      - redis key outdated -> no cache, so all requests hit DB (e.g. Mysql) directly
      - use redis lock deal with above (e.g. client -> redis <-- update cache --> redis lock (only can access mysql when get lock, then update cache) -> Mysql)
  - Avoid deadlock
    - solution : add lock expire time (e.g. : expire lock 20)
    - cases
      - a server gets a lock, then server down, so server has NO WAY to release its lock -> cause deadlock
      - 不可重入
    - https://youtu.be/h_thAi4SCEQ?si=QAEFji03rR1n92Tp&t=72
    - https://youtu.be/zt-qeYfQvJc?si=qIDLto7ro5Ru93v_&t=32
  - pros
  - cons
    - across progresses, across services, across instances

### 5) Distribution lock

- Implementation way
  - with Redis
  - with ZK (zookeeper or etcd)
  - with MySQL

- Features
  - exclusive(獨佔排他使用)
    - Redis :
      - lock : setnx
      - unlock : del
      - retry : recursion call or while loop
        - [code](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/service/StockServiceRedisDistributionLock.java)
  - 原子性 (ATOM, Atomicity)
    - https://youtu.be/h_thAi4SCEQ?si=f1spko6XNuhX6TKx&t=548
    - make sure ATOM when `get lock` and `set up expire time`
    - redis cmd : `set <key> <value> ex <expire_time> nx`
      - [sql](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_expire_time_lock.sql)
    - however, need to make sure ATOM within `check same lock` and `release lock`
        - -> use `Lua script` (redis default script language) (can send multiple cmd to redis at once)
        - [lua code cmd](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/lua_cmd.lua)
  - 防誤刪
    - Only thread which receive lock can unlock it
    - UUID + check first, then delete
  - 可重入性
    - JUC (java concurrency) ReentrantLock
      - `Lock Steps`: ReentrantLock.lock() -> NonFairSync.lock() -> AQS.acquire() -> NonFairSync.tryAcquire(1) ->  sync.NonFairTryAcquire(1)
        - 1. CAS gets lock, if not one owns lock (state==0), gets lock success, record which thread acquires lock (2 op)
        - 2. if state != 0, means lock already being used, check if current thread is with lock, if yes, 重入 (state += 1)
        - 3. or, lock failed, insert to queue, and wait
      - `Unlock Steps`:  ReentrantLock.unlock() -> AQS.release(1) -> sync.tryRelease(1)
        - 1. check if current thread is with lock, if not, throw exception
        - 2. state-=1, check if state==0, if true, unlock success, return true
        - 3. if false, unlock failed, return false
      - Implementation : Redis Hash data structure + Lua script
        - https://youtu.be/V-OREDbG2UA?si=eh_tFgXkNRd8woOP
    - https://youtu.be/zt-qeYfQvJc?si=cGPlfxi9AGqOPiXq&t=282
  - 自動續期
    - automatically refresh lock expire time
    - to avoid if lock expired before op code completed

## 6) Ref

- Course
    - Video
        - https://youtu.be/CcMVMRTS-eU?si=4EsXFRmyP6BmdVFF
    - code