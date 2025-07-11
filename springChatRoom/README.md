# Spring Chat Room

Build a chat system with webSocket, run in Spring boot system.

- UI : http://localhost:8080


<p align="center"><img src ="./doc/pic/chat_room_1.png"></p>

<p align="center"><img src ="./doc/pic/chat_room_2.png"></p>

## Tech
- Java 17
- spring boot 3.0.12
- webSocket
- STOMP
- FE
  - SockJS (webSocket client)
- Cluster version
    - Redis

## Run (single)

<details>
<summary>Run (single)</summary>

```bash
# compile
mvn package -DskipTests
# run app
java -jar target/springChatRoom-0.0.1-SNAPSHOT.jar
```
</details>

## Run (cluster)

<details>
<summary>Run (single)</summary>

```bash

# install redis
# https://github.com/yennanliu/utility_shell/blob/master/redis/install_redis_mac.sh

# run redis
brew services start redis
# test (local)
redis-cli ping
# to the redis shell (local)
redis-cli

# compile
mvn package -DskipTests
# run app
java -jar target/springChatRoom-0.0.1-SNAPSHOT.jar
```
</details>

## Todo
- Feature
  - show user(online/offline) list
  - private msg (1 to 1)
  - setup group, group chat
  - @ "notification"
  - msg push (?)
  - "read" 已讀 feature
  - save history msg
  - broadcast msg
  - push msg to users (offer API)
  - offer user counts/online users (offer API)

## Knowledge

- WebSocket
    - Server can send info to client (bi-directon communicartion, TCP protocol)
        - 3 ways handshake
        - server, client side can send/receive msg, and terminate connection
    - long connection between server and client, no need to check with client every time
    - more efficient when communicating (header is shorter)

- Use case of WebSocket
    - chat room
    - online game
    - geo location app
    - stock market app
    - co-editing system (e.g. google doc ?)
    - app needs near real-time update/sync, data transmit is not small

## Reference

- redisTemplate API cmd
  - https://ost.51cto.com/posts/2333
s