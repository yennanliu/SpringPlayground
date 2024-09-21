<template>
  <div>
    <h1>Create New PlayList</h1>
    <!-- <button @click="authorize">
      Authorize with Spotify
    </button> -->
    <div>
      <div>
        <label for="playlistName">New Playlist Name:</label>
        <input
          type="text"
          id="playlistName"
          v-model="newPlayList.name"
          placeholder="Enter playlist name"
        />
        <button @click="createPlaylist">Create Playlist</button>
        <div v-if="playlistCreated">Playlist created successfully!</div>
      </div>
      <!-- <div>
        <label for="playlistId">Playlist ID:</label>
        <input
          type="text"
          id="playlistId"
          v-model="newSongToList.playlistId"
          placeholder="Enter playlist ID"
        />
        <button @click="addSongToPlayList">Add Song To Playlist</button>
        <div v-if="newSongsAdded">Song added to Playlist successfully!</div>
      </div> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      //authorized: false,
      playlistCreated: false,
      newSongsAdded: false,
      accessToken: null,
      newPlayList: { userId: "someId", name: "", authCode: "code" },
      newSongToList: {
        playlistId: "",
        trackURIs: "",
        authCode: "code",
      },
    };
  },
  _methods: {
    // async authorize() {
    //   try {
    //     const response=await fetch("http://localhost:8888/authorize");
    //     if(!response.ok) {
    //       throw new Error("Failed to authorize with Spotify");
    //     }
    //     const data=await response.json();
    //     if(data.url) {
    //       window.location.href=data.url; // Redirect to the Spotify authorization page
    //     } else {
    //       throw new Error("Redirect URI not found in response");
    //     }
    //   } catch(error) {
    //     console.error(error);
    //     // Handle error
    //   }
    // },

    async createPlaylist() {
      try {
        console.log("createPlaylist start");
        const urlParams=new URLSearchParams(window.location.search);
        const code=urlParams.get("code");
        if(!code) {
          throw new Error("Authorization code not found");
        }
        this.newPlayList.authCode=code;
        const response=await axios.post(
          "http://localhost:8888/playlist/create",
          this.newPlayList
        );
        if(response.status===200) {
          this.playlistCreated=true;
        } else {
          throw new Error("Failed to create playlist");
        }
      } catch(error) {
        console.error(error);
        // Handle error
      }
    },

    // async addSongToPlayList() {
    //   try {
    //     console.log("addSongToPlayList start");
    //     const urlParams=new URLSearchParams(window.location.search);
    //     const code=urlParams.get("code");
    //     if(!code) {
    //       throw new Error("Authorization code not found");
    //     }
    //     this.newSongToList.authCode=code;
    //     // TODO : instead of hardcode trace uris, get them from UI
    //     this.newSongToList.trackURIs=
    //       "spotify:track:1wHrcMSpzIbNk4CipbKft0,spotify:track:69TMjHuBaLNRtMPopKWbdC,spotify:track:2Ugyo7kjFFli9gHf9KfK5A,spotify:track:3xsFZOyd6mfrjZT1Sf4nXR,spotify:track:6gWRznlX7vaUW0r8KF9iMZ,spotify:track:4kuKGST6Pj4iMZBpO6BYl4,spotify:track:1dO38CsQliftngGVX2NwI2,spotify:track:4KU4UDuuZEjiFGP01OkF9H";

    //     const response=await fetch("http://localhost:8888/playlist/addSong", {
    //       method: "POST",
    //       headers: {
    //         "Content-Type": "application/json",
    //       },
    //       body: JSON.stringify({
    //         songUris: this.newSongToList.trackURIs,
    //         authCode: code,
    //         playlistId: this.newSongToList.playlistId,
    //       }),
    //     });
    //     if(response.status===200) {
    //       this.newSongsAdded=true;
    //     } else {
    //       throw new Error("Failed to add song to playlist");
    //     }
    //   } catch(error) {
    //     console.error(error);
    //     // Handle error
    //   }
    // },
  },
  get methods() {
    return this._methods;
  },
  set methods(value) {
    this._methods=value;
  },
  mounted() {
    //const urlParams = new URLSearchParams(window.location.search);
    // const code = urlParams.get("code");
    // if (code) {
    //   this.authorized = true;
    // }
  },
};
</script>