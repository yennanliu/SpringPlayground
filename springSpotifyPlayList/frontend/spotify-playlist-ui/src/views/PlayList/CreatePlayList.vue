<template>
  <div class="create-playlist-container">
    <div class="create-playlist-card">
      <div class="playlist-header">
        <i class="fas fa-plus-circle header-icon"></i>
        <h1>Create New Playlist</h1>
        <p class="subtitle">Make a new collection for your favorite tracks</p>
      </div>

      <div class="form-container">
        <div class="form-group">
          <label for="playlistName">
            <i class="fas fa-music"></i> Playlist Name
          </label>
          <input
            type="text"
            id="playlistName"
            v-model="newPlayList.name"
            placeholder="Enter a name for your playlist..."
            required
          />
        </div>

        <button 
          class="create-button" 
          @click="createPlaylist"
          :disabled="!newPlayList.name"
        >
          <i class="fas fa-plus"></i> Create Playlist
        </button>
      </div>

      <!-- Success message with animation -->
      <div v-if="playlistCreated" class="message success">
        <i class="fas fa-check-circle"></i>
        <span>Playlist created successfully!</span>
      </div>

      <!-- Error message with animation -->
      <div v-if="playlistCreationError" class="message error">
        <i class="fas fa-exclamation-circle"></i>
        <span>{{ playlistCreationError }}</span>
      </div>
    </div>

    <div class="playlist-tips">
      <h3><i class="fas fa-lightbulb"></i> Tips for Great Playlists</h3>
      <ul>
        <li>Choose a descriptive name for your playlist</li>
        <li>Use the search tool to find tracks to add</li>
        <li>Add variety to create the perfect flow</li>
        <li>Share your playlist with friends</li>
      </ul>
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
      playlistCreationError: null,
      newPlayList: { userId: "someId", name: "", authCode: "code" },
    };
  },
  methods: {
    async createPlaylist() {
      try {
        this.playlistCreated = false;
        this.playlistCreationError = null;
        
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
          this.newPlayList.name = ""; // Reset form
        } else {
          throw new Error("Failed to create playlist");
        }
      } catch (error) {
        console.error(error);
        this.playlistCreationError = error.message;
      }
    },
  },
};
</script>

<style scoped>
.create-playlist-container {
  max-width: 900px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.create-playlist-card {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 16px;
  padding: 2.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
}

.create-playlist-card:before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 5px;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
}

.playlist-header {
  text-align: center;
  margin-bottom: 2rem;
}

.header-icon {
  font-size: 3rem;
  color: var(--spotify-green);
  margin-bottom: 1rem;
}

.playlist-header h1 {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: var(--spotify-light-gray);
  font-size: 1.1rem;
}

.form-container {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: var(--spotify-white);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.1);
  color: var(--spotify-white);
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  background-color: rgba(255, 255, 255, 0.15);
  box-shadow: 0 0 0 2px rgba(29, 185, 84, 0.5);
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.create-button {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 50px;
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.create-button:hover {
  background-color: #1ed760;
  transform: translateY(-2px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.create-button:disabled {
  background-color: #666;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.message {
  margin-top: 1.5rem;
  padding: 1rem;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  animation: slideIn 0.3s ease forwards;
}

.success {
  background-color: rgba(29, 185, 84, 0.1);
  border-left: 4px solid var(--spotify-green);
  color: var(--spotify-green);
}

.error {
  background-color: rgba(255, 81, 81, 0.1);
  border-left: 4px solid #ff5151;
  color: #ff5151;
}

.playlist-tips {
  background-color: rgba(40, 40, 40, 0.3);
  border-radius: 16px;
  padding: 2rem;
  align-self: start;
}

.playlist-tips h3 {
  font-size: 1.3rem;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--spotify-green);
}

.playlist-tips ul {
  padding-left: 1.5rem;
}

.playlist-tips li {
  margin-bottom: 0.75rem;
  color: var(--spotify-light-gray);
  font-size: 0.9rem;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive styles */
@media (max-width: 768px) {
  .create-playlist-container {
    grid-template-columns: 1fr;
  }

  .playlist-tips {
    margin-top: 1rem;
  }
}
</style>