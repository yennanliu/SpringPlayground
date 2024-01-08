
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

## API

| API                                       | Type                                    | Purpose | Example cmd | Comment|  
|-------------------------------------------|-----------------------------------------| ---- | ----- | ---- |  
| http://localhost:7777/stock/deduct        | lock test endpoint                      | | |
| http://localhost:8080/stock/deduct        | Nginx endpoint                          | | |
| http://localhost:8080/test/fair/lock/{id} | test Redisson Fair lock (Nignx endpoint) | | |
| http://localhost:8080/test/read/lock      | test Redisson read lock (Nignx endpoint) | | |
| http://localhost:8080/test/write/lock     | test Redisson write lock (Nignx endpoint) | | |
| http://localhost:8080/test/semaphore      | test Redisson semaphore (Nignx endpoint) | | |
| http://localhost:8080/test/countdown      | test Redisson countdown (Nignx endpoint) | | |
| http://localhost:8080/test/latch          | test Redisson latch (Nignx endpoint)    | | |
| http://localhost:8080/test/zk             | test ZK lock (Nignx endpoint)           | | |


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

- Implementation:

  - with `Redis`
    - [video](https://youtu.be/LCxDMbnU_M0?si=QiAdiFj2Q2t3lGVB&t=510)
    - LOCK
      - V1: `setnx` : 獨佔排他, 死鎖, 不可重入, 原子性
      - V2: `set k v 30 nx` : 獨佔排他, 死鎖, 不可重入
      - `V3: hash + lua script : 可重入鎖` (preferable) ******
        - step 1: check if lock is owned (exists), if not, then get lock (hset/hincrby) and set expire time
        - step 2: if lock is owned, check if owned by current thread (hexists), if true, then 重入  (hincrby), and update expire time (expire)
        - step 3: if above is false, then get lock failed, retry
        - [code](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/lock/DistributedRedisLock.java)
      - `V4: timer定時器 + Lua script + lock expire time auto renew`
        - check if current thread's lock (hexists=1), if true, renew expire time
      - UNLOCK
        - V1: `del` : 導致誤刪
        - V2: check, then delete and make sure ATOM : Lua script
        - `V3: hash + Lua script : 可重入` (preferable) ******
          - step 1: check if current thread's lock existed, if not, return nil, throw exception
          - step 2: if existed, (hincrby-1), check if hincrby-1 == 0, if yes, release lock, return true
          - step 3: if above is false, return false

  - with `ZK (zookeeper or etcd)`

  - with `MySQL`

### 6) Distribution lock (`Redis`)

- Features
  - [summary](https://youtu.be/LCxDMbnU_M0?si=tZ6As-xkdoFwgGIu&t=120)
  - a) exclusive(獨佔排他使用)
    - Redis :
      - lock cmd : `setnx`
      - unlock cmd : `del`
      - retry : recursion call or while loop
        - [code](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/service/StockServiceRedisDistributionLock.java)
  - b) 原子性 (ATOM, Atomicity)
    - https://youtu.be/h_thAi4SCEQ?si=f1spko6XNuhX6TKx&t=548
    - make sure ATOM when `get lock` and `set up expire time`
    - redis cmd : `set <key> <value> ex <expire_time> nx`
      - [sql](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_expire_time_lock.sql)
    - however, need to make sure ATOM within `check same lock` and `release lock`
        - -> use `Lua script` (redis default script language) (can send multiple cmd to redis at once)
        - [lua code cmd](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/lua_cmd.lua)
  - c) 防誤刪
    - Only thread which receive lock can unlock it
    - UUID + check first, then delete
  - d) 可重入性
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
        - Id = UUID + thread-id (key field value)
        - [sql](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_reentrantLock_lua.sql)
        - https://youtu.be/V-OREDbG2UA?si=eh_tFgXkNRd8woOP
        - Id = UUID + thread-id
    - https://youtu.be/zt-qeYfQvJc?si=cGPlfxi9AGqOPiXq&t=282
  - e) 自動續期
    - timer + lua script
    - [sql](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_auto_renew_expire_time_lua.sql)
    - automatically refresh lock expire time
    - to avoid if lock expired before op code completed


### 7) RedLock algorithm (Redis) (紅鎖算法)

- deal with lock failure in redis cluster (node1, node2 .. ) (no master-slave relation)
- [video](https://youtu.be/_v86mnwBVYg?si=LYcF52YbkkBcn8FL&t=233)
- [video](https://youtu.be/bSScx1TKtFk?si=H34YveIxiwl-zdxi&t=227)


### 8) `Redisson : Redis Java client` *****

- Java client with Redis distribution lock feature
- Setup steps
  - step 1: add dep
  - step 2: config
    - [RedissonConfig](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/config/RedissonConfig.java)
    - [add client to Spring container (IOC)](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/src/main/java/com/yen/SpringDistributionLock/service/StockServiceRedissonReadWriteLock.java#L27)
  - step 3: use
    - `ReentrantLock lock :  redissonClient.getLock("xxx");`
    - `fair lock : RReadWriteLock rwLock = redissonClient.getFairLock("xxx");`
    - `readwrite lock: `
      - `RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");`
      - `rwLock.readLock.lock();` // or unlock
      - `rwLock.writeLock.lock();` // or unlock
    - 信號量 (Semaphore)
      - Rsemaphore semaphore = redissionClient.getSemaphore("xxx");
      - semaphore.trySetPermits(3);
      - semaphore.acquire(); // or release
    - 閉鎖 (countDownLatch)
      - RCountDownLatch cdl = redissionClient.getCountDownLatch("xxx");
      - cdl.trySetCount(6);
      - cdl.await(); // or countDown

- https://github.com/redisson/redisson
- https://github.com/redisson/redisson/wiki/Table-of-Content
- https://youtu.be/iZjxpEv2jSg?si=H29OQTTM2SRfCLPQ&t=31


### 9) Zookeeper

- [basic cmd](https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/zk_cmd.sh)

- Node type
  - 永久節點
    - `create /path content`
    - still exists when shutdown ZK
  - 臨時節點
    - `create -e /path content`
    - deleted when shutdown ZK
  - 永久序列化節點
    - `create -s /path content`
    - still exists when shutdown ZK (serialization node)
  - 臨時序列化節點
    - `create -s -e /path content`
    - deleted when shutdown ZK (serialization node)

- Node event monitor (only once)
  - Node creation monitoring
    - `stat -w /xx` 
  - Node deletion monitoring
    - `stat -w /xx`
  - Node data change monitoring
    - `get -w /bb`
  - sub Node change monitoring
    - `ls -w /xx`

- Java client
  - official client (org.apache.zookeeper)
  - zkClient : has many foundation implementation, used by Kafka, Dubbo..
  - Curator : similar to Redisson, has distribution lock implementation


## 11) ZK distribution lock

- Features
  - 獨佔排他 (exclusion) : 自旋鎖
    - 利用 `ZK node 無法重覆特性`
    - via node creation (only 1 thread can create 1 node (and only once))
  ```bash
  [zk: localhost:2181(CONNECTED) 52] create /locks/lock
  Created /locks/lock
  [zk: localhost:2181(CONNECTED) 54] create /locks/lock
  Node already exists: /locks/lock
  ```

  - 阻塞鎖
    - 利用 `ZK 臨時序列化節點`



## 20) Ref

- Course
    - Video
        - https://youtu.be/CcMVMRTS-eU?si=4EsXFRmyP6BmdVFF
    - code