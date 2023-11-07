# Spring Chat Room

Build a chat system with webSocket, run in Spring boot system.

- UI : http://localhost:8080

## Tech
- Java 11
- spring boot 3.0.12
- webSocket
- STOMP
- FE
  - SockJS (webSocket client)
- Extension
    - Redis

## Run
```bash
mvn package -DskipTests
java -jar target/springChatRoom-0.0.1-SNAPSHOT.jar
```

## Todo
- Feature
  - private msg (1 to 1)
  - setup group, group chat
  - @ "notification"
  - msg push (?)
  - "read" 已讀 feature
  - show user list
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