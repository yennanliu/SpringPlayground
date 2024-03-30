<template>
  <div>
    <h1>Spotify Authorization</h1>
    <button v-if="!authorized" @click="authorize">
      Authorize with Spotify
    </button>
    <button v-if="authorized" @click="createPlaylist">Create Playlist</button>
    <div v-if="playlistCreated">Playlist created successfully!</div>
  </div>
</template>

<script>
var axios = require("axios");
//import swal from "sweetalert";

export default {
  data() {
    return {
      authorized: false,
      playlistCreated: false,
      accessToken: null,
      spotifyAuthCode: null,
      newPlayList: { userId: "someId", name: "someName" },
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
          console.log(">>> data.url = {}", data.url);
          window.location.href = data.url; // Redirect to the Spotify authorization page
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },

    // async exchangeCodeForToken() {
    //   try {
    //     const response = await axios.post("http://localhost:8888/exchange", {
    //       code: this.spotifyAuthCode,
    //     });
    //     if (response.data.access_token) {
    //       this.accessToken = response.data.access_token;
    //       this.authorized = true;
    //     } else {
    //       throw new Error("Access token not found in response");
    //     }
    //   } catch (error) {
    //     console.error(error);
    //     // Handle error
    //   }
    // },

    async createPlaylist() {
      try {
        console.log("createPlaylist start");
        await axios.post(
          "http://localhost:8888/playlist/create",
          this.newPlayList
        );
        console.log("Playlist created successfully!");
        this.playlistCreated = true;
      } catch (error) {
        console.error(error);
        console.log("createPlaylist error : {}", error);
        // Handle error
      }
    },
  },

  mounted() {
    this.authorize();
    this.createPlaylist();
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    if (code) {
      // Do something with the code
      console.log("Authorization code:", code);
    }

    // const urlParams = new URLSearchParams(window.location.search);
    // this.spotifyAuthCode = urlParams.get("code");
    // if (this.spotifyAuthCode) {
    //   this.exchangeCodeForToken();
    // }
  },
};
</script>
