<template>
  <div>
    <h1>Create New Playlist</h1>
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
        <!-- Success message -->
        <div v-if="playlistCreated">Playlist created successfully!</div>
        <!-- Error message -->
        <div v-if="playlistCreationError" style="color: red">
          {{ playlistCreationError }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["baseURL"],
  data() {
    return {
      playlistCreated: false,
      playlistCreationError: null, // To track errors
      newPlayList: { userId: "someId", name: "", authCode: "code" },
    };
  },
  methods: {
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
          `${this.baseURL}/playlist/create`,
          this.newPlayList
        );
        if (response.status === 200) {
          this.playlistCreated = true;
          this.playlistCreationError = null; // Clear error if successful
        } else {
          throw new Error("Failed to create playlist");
        }
      } catch (error) {
        console.error(error);
        this.playlistCreationError = error.message; // Set error message
      }
    },
  },
};
</script>