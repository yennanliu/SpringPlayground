<template>
  <div>
    <h1>Spotify Authorization</h1>
    <button v-if="!authorized" @click="authorize">
      Authorize with Spotify
    </button>
    <button v-if="!authorized" @click="createPlaylist">Create Playlist</button>
    <div v-if="playlistCreated">Playlist created successfully!</div>
    <button v-if="!authorized" @click="addSongToPlayList">Add Song To Playlist</button>
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
      newSongToList: { playlistId: "playlistId", songUris: ["url_1", "url_2"], authCode: "code" },
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
        const response = await axios.post(
          "http://localhost:8888/playlist/addSong",
          this.newSongToList
        );
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
