# SpringSpotify PlayList

<p align="center"><img src ="./doc/pic/spotify_app_1.png"></p>

<p align="center"><img src ="./doc/pic/spotify_app_2.png"></p>

<p align="center"><img src ="./doc/pic/spotify_app_3.png"></p>

<p align="center"><img src ="./doc/pic/spotify_app_4.png"></p>

> Generate a Spotify playlist based on input values or recommendations using the Spotify API.

> Pain point
	- `Spotify continues to recommend similar songs, but users want to explore new genres or styles.`

> Solution
	- Create a new playlist and use the Spotify API to fetch song parameters. Then, use the recommendation API to get a new playlist of recommended songs.

Steps:

1. Manually create a new playlist.
2. Use the get-audio-features API to retrieve parameters.
3. Use the get-recommendations API to get a new playlist of recommended songs.
4. Repeat the process as needed.


## Run

<details>
<summary>Run</summary>

### Run via Docker
```bash
docker-compose up
```

### Run manually
```bash

#------------------------
# BE
#------------------------
# build
cd springSpotifyPlayList/backend/SpotifyPlayList
mvn package

# run
java -jar target/springSpotifyPlayList-0.0.1-SNAPSHOT.jar


#------------------------
# FE
#------------------------
cd /springSpotifyPlayList/frontend/spotify-playlist-ui
npm run serve
```

- Clean docker data
```bash
docker rm -f $(docker ps -aq)
# Remove all images
docker rmi -f $(docker images -q)
# remove all containers in docker
docker rm -f $(docker ps -a -q)
# remove all images in docker
docker rmi -f $(docker images -q -a)
```

</details>

## Architecture

## Technology

- Backend : Java
- Frontend : Vue


## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:8888/swagger-ui.html |  BE API | | |
| http://localhost:8080 |  FE App UI | | |


## Important Concepts

## Reference

<details>
<summary>Reference</summary>

- Java client
	- https://github.com/spotify-web-api-java/spotify-web-api-java
- Doc
	- https://spotify-web-api-java.github.io/spotify-web-api-java/

- Libaray
	- https://github.com/spotify-web-api-java/spotify-web-api-java
	- Recommendation
		- https://github.com/spotify-web-api-java/spotify-web-api-java/blob/76d69b152cb17e7b8d7ea56b58f0a9b078774708/examples/data/browse/GetRecommendationsExample.java#L5
		- https://spotify-web-api-java.github.io/spotify-web-api-java/se/michaelthelin/spotify/model_objects/specification/Recommendations.html

- Recommendations API
	- https://developer.spotify.com/documentation/web-api/reference/get-recommendations

- get song feature
	- https://developer.spotify.com/documentation/web-api/reference/get-audio-features

- Code example
	- https://jitpack.io/p/lbengzon/spotify-web-api-java
	- https://github.com/yennanliu/nelson/blob/master/server.js#L88

- Other project
	- https://nelson.glitch.me/#
	- https://github.com/hardikSinghBehl/spotifyApiSpring/tree/master


- ML ref notebook
	- https://github.com/yennanliu/SpringPlayground/blob/main/springSpotifyPlayList/doc/Spotify_ApI_call_demo.ipynb?fbclid=IwAR1ZhL081euAUCeB54kaMMNqCHBN1HnuLLTYpnpjNHAf4MMFW8VkgdP5N1o


</details>

## TODO
