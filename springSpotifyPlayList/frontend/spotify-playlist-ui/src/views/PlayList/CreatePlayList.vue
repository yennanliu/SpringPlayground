<template>
  <div>
    <h1>Spotify Authorization</h1>
    <button v-if="!authorized" @click="authorize">
      Authorize with Spotify
    </button>
    <button v-if="!authorized" @click="createPlaylist">Create Playlist</button>
    <div v-if="playlistCreated">Playlist created successfully!</div>
    <button v-if="!authorized" @click="addSongToPlayList">
      Add Song To Playlist
    </button>
    <div v-if="newSongsAdded">Song added to Playlist successfully!</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      authorized: false,
      playlistCreated: false,
      newSongsAdded: false,
      accessToken: null,
      newPlayList: { userId: "someId", name: "someName", authCode: "code" },
      newSongToList: {
        playlistId: "playlistId",
        trackURIs: "",
        authCode: "code",
      },
    };
  },
  methods: {
    async authorize() {
      try {
        const response = await fetch("http://localhost:8888/authorize");
        if (!response.ok) {
          throw new Error("Failed to authorize with Spotify");
        }
        const data = await response.json();
        if (data.url) {
          window.location.href = data.url; // Redirect to the Spotify authorization page
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },

    async createPlaylist() {
      try {
        console.log("createPlaylist start");
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get("code");
        if (!code) {
          throw new Error("Authorization code not found");
        }
        this.newPlayList.authCode = code;
        const response = await axios.post(
          "http://localhost:8888/playlist/create",
          this.newPlayList
        );
        if (response.status === 200) {
          this.playlistCreated = true;
        } else {
          throw new Error("Failed to create playlist");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },

    async addSongToPlayList() {
      try {
        console.log("addSongToPlayList start");
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get("code");
        if (!code) {
          throw new Error("Authorization code not found");
        }
        this.newSongToList.authCode = code;
        this.newSongToList.trackURIs =
          "spotify:track:1wHrcMSpzIbNk4CipbKft0,spotify:track:69TMjHuBaLNRtMPopKWbdC,spotify:track:2Ugyo7kjFFli9gHf9KfK5A,spotify:track:3xsFZOyd6mfrjZT1Sf4nXR,spotify:track:6gWRznlX7vaUW0r8KF9iMZ,spotify:track:4kuKGST6Pj4iMZBpO6BYl4,spotify:track:1dO38CsQliftngGVX2NwI2,spotify:track:4KU4UDuuZEjiFGP01OkF9H";

        // const response = await axios.post(
        //   "http://localhost:8888/playlist/addSong",
        //   this.newSongToList
        // );
        // if (response.status === 200) {
        //   this.newSongsAdded = true;
        // } else {
        //   throw new Error("Failed to add song to playlist");
        // }

        const response = await fetch("http://localhost:8888/playlist/addSong", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            songUris: this.newSongToList.trackURIs,
            authCode: code,
            playlistId: "yyy", // this.playlistId,
          }),
        });
        if (response.status === 200) {
          this.newSongsAdded = true;
        } else {
          throw new Error("Failed to add song to playlist");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },
  },
  mounted() {},
};
</script>
