# springSpotifyPlayList

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

- https://developer.spotify.com/documentation/web-api/reference/get-recommendations - recommendations API
- https://developer.spotify.com/documentation/web-api/reference/get-audio-features - get song feature
- https://github.com/yennanliu/nelson/blob/master/server.js#L88
- https://nelson.glitch.me/#

## TODO
