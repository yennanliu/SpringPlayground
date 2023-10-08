# Spring Chat Room

## Run
```bash
```

## Tech
- spring boot
- Spring socket

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

## Ref
- Article
	- https://ithelp.ithome.com.tw/articles/10197142
	- https://ithelp.ithome.com.tw/articles/10197191
	- https://www.baeldung.com/websockets-spring
	- https://blog.csdn.net/wwd0501/article/details/54582912
	- https://www.slideshare.net/wenhsiaoyi/java-api-for-websocket
	- https://www.syscom.com.tw/ePaper_New_Content.aspx?id=368&EPID=194&TableName=sgEPArticle
	- https://tw511.com/a/01/16646.html
- Code
	- https://spring.io/guides/gs/messaging-stomp-websocket/
	- https://morosedog.gitlab.io/springboot-20190416-springboot28/

- Video
	- https://youtu.be/r4fdPmZuzmY?si=qrmRTm09Bo53Ina1
	- https://youtu.be/RbCFeeePJoM?si=DF6mES5XApvSkhXw
	- https://youtu.be/es_fTKyfI4w?si=vVhEz6Us-zrcerTR