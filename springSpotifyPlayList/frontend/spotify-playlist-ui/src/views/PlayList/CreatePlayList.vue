<template>
  <div class="container">
    <h1 class="text-center">Create New Playlist</h1>
    <div class="form-container">
      <div class="form-group">
        <label for="playlistName" class="form-label">New Playlist Name:</label>
        <input
          type="text"
          id="playlistName"
          class="form-control form-control-lg"
          v-model="newPlayList.name"
          placeholder="Enter playlist name"
          required
        />
      </div>
      <button class="btn btn-outline-light btn-lg custom-btn" @click="createPlaylist">
        Create Playlist
      </button>
      <!-- Success message -->
      <div v-if="playlistCreated" class="alert alert-success mt-3">
        Playlist created successfully!
      </div>
      <!-- Error message -->
      <div v-if="playlistCreationError" class="alert alert-danger mt-3">
        {{ playlistCreationError }}
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

<style scoped>
.container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #f9f9f9;
  border-radius: 10px;
}

.form-container {
  margin-top: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-control-lg {
  padding: 10px;
  font-size: 1.25rem;
}

.btn-outline-light {
  border-color: #1db954;
  color: #1db954;
  font-size: 1.2rem;
  font-weight: bold;
}

.custom-btn {
  display: block;
  width: 100%;
  max-width: 300px;
  margin: 0 auto;
}

.alert {
  text-align: center;
  margin-top: 20px;
}

.alert-success {
  color: #28a745;
}

.alert-danger {
  color: #dc3545;
}
</style>