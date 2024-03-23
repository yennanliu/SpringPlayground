# SpringSpotify PlayList

Generate spotify playlist based on input values / recommendation with Spotify API

解決痛點: Spotify持續推薦類似歌曲, 想要換口味 但還是被推播舊的風格歌曲

解決構想: 透過新創立的歌單, 讓Spotify獲取歌曲參數, 透過推薦API 取得新的推薦歌單


步驟: 

```
手動創立新歌單 -> get-audio-features API 取得參數 -> get-recommendations 取得推薦歌單 -> 持續步驟 ...
```

## Technology
- Java

## Data Model


## Run (local)


## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
|  |  |  || |

## Important Concepts

## Ref

<details>
<summary>Structure</summary>

- Libaray
	- https://github.com/spotify-web-api-java/spotify-web-api-java

- ecommendations API
	- https://developer.spotify.com/documentation/web-api/reference/get-recommendations
- get song feature
	- https://developer.spotify.com/documentation/web-api/reference/get-audio-features
- Java client
	- https://github.com/spotify-web-api-java/spotify-web-api-java
- Code example
	- https://jitpack.io/p/lbengzon/spotify-web-api-java
	- https://github.com/yennanliu/nelson/blob/master/server.js#L88
- Other project
	- https://nelson.glitch.me/#
	- https://github.com/hardikSinghBehl/spotifyApiSpring/tree/master

</details>

## TODO
