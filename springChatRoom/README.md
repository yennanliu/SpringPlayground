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