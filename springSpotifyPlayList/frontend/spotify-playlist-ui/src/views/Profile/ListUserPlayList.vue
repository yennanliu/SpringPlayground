<template>
  <div class="playlist-page">
    <div class="page-header">
      <h1>Your Playlists</h1>
      <div class="playlist-stats">
        <div class="stat-item">
          <span class="stat-count">{{ playLists.length }}</span>
          <span class="stat-label">Playlists</span>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Loading your playlists...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="msg" class="error-container">
      <i class="fas fa-exclamation-circle"></i>
      <p>{{ msg }}</p>
      <button @click="fetchData" class="retry-button">
        <i class="fas fa-sync-alt"></i> Try Again
      </button>
    </div>

    <!-- Empty State -->
    <div v-else-if="playLists.length === 0" class="empty-container">
      <i class="fas fa-music"></i>
      <h2>No Playlists Found</h2>
      <p>Create your first playlist to get started</p>
      <router-link to="/playlist" class="create-playlist-btn">
        <i class="fas fa-plus"></i> Create Playlist
      </router-link>
    </div>

    <!-- Playlists Grid -->
    <div v-else class="playlists-grid">
      <div 
        v-for="playList in playLists" 
        :key="playList.id" 
        class="playlist-card"
      >
        <div class="playlist-image-container">
          <img
            v-if="playList.images && playList.images.length > 0"
            :src="playList.images[0].url"
            :alt="playList.name"
            class="playlist-image"
          />
          <div v-else class="playlist-fallback-image">
            <i class="fas fa-music"></i>
          </div>
          
          <div class="playlist-overlay">
            <a 
              v-if="playList.externalUrls && playList.externalUrls.externalUrls && playList.externalUrls.externalUrls.spotify"
              :href="playList.externalUrls.externalUrls.spotify"
              target="_blank"
              class="play-button"
              title="Open in Spotify"
            >
              <i class="fas fa-play"></i>
            </a>
          </div>
        </div>
        
        <div class="playlist-info">
          <h3 class="playlist-name">{{ playList.name }}</h3>
          
          <div class="playlist-meta">
            <div class="playlist-tracks" v-if="playList.tracks">
              <i class="fas fa-music"></i> {{ playList.tracks.total || 0 }} tracks
            </div>
            
            <div class="playlist-id">
              <i class="fas fa-fingerprint"></i>
              <span class="id-text">{{ playList.id }}</span>
            </div>
          </div>
          
          <div class="playlist-actions">
            <a 
              v-if="playList.externalUrls && playList.externalUrls.externalUrls && playList.externalUrls.externalUrls.spotify"
              :href="playList.externalUrls.externalUrls.spotify"
              target="_blank"
              class="spotify-link"
            >
              <i class="fab fa-spotify"></i> Open in Spotify
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
var axios = require("axios");
export default {
  props: ["baseURL"],
  name: "ListUserPlayList",
  data() {
    return {
      playLists: [],
      msg: null,
      loading: true
    };
  },
  methods: {
    async fetchData() {
      this.loading = true;
      this.msg = null;
      
      try {
        const response = await axios.get(`${this.baseURL}/user_data/playlist/`);
        this.playLists = response.data;
        console.log(">>> (fetchData) this.playLists =", JSON.stringify(this.playLists));
      } catch (error) {
        console.error(error);
        this.msg = "Failed to fetch playlists. Please try again.";
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
.playlist-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 0;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 3rem;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.page-header h1 {
  font-size: 2.5rem;
  font-weight: 800;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
}

.playlist-stats {
  display: flex;
  gap: 2rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-count {
  font-size: 2rem;
  font-weight: 700;
  color: var(--spotify-green);
}

.stat-label {
  font-size: 0.9rem;
  color: var(--spotify-light-gray);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* Loading Container */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem 0;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid rgba(255, 255, 255, 0.1);
  border-left-color: var(--spotify-green);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-container p {
  font-size: 1.2rem;
  color: var(--spotify-light-gray);
}

/* Error Container */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem 0;
  color: #ff5151;
}

.error-container i {
  font-size: 3rem;
  margin-bottom: 1.5rem;
}

.error-container p {
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}

.retry-button {
  background: none;
  border: 2px solid var(--spotify-green);
  color: var(--spotify-green);
  padding: 0.75rem 1.5rem;
  border-radius: 50px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.retry-button:hover {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
}

/* Empty State */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem 0;
  color: var(--spotify-light-gray);
}

.empty-container i {
  font-size: 4rem;
  margin-bottom: 1.5rem;
  opacity: 0.5;
}

.empty-container h2 {
  font-size: 1.8rem;
  margin-bottom: 0.5rem;
}

.empty-container p {
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.create-playlist-btn {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  border: none;
  border-radius: 50px;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  transition: all 0.3s ease;
}

.create-playlist-btn:hover {
  background-color: #1ed760;
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

/* Playlists Grid */
.playlists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
}

.playlist-card {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
}

.playlist-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.playlist-image-container {
  position: relative;
  aspect-ratio: 1;
  overflow: hidden;
}

.playlist-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.playlist-card:hover .playlist-image {
  transform: scale(1.05);
}

.playlist-fallback-image {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #333 0%, #222 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.playlist-fallback-image i {
  font-size: 3rem;
  color: rgba(255, 255, 255, 0.2);
}

.playlist-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.playlist-card:hover .playlist-overlay {
  opacity: 1;
}

.play-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  transform: translateY(20px);
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
}

.playlist-card:hover .play-button {
  transform: translateY(0);
}

.play-button:hover {
  transform: scale(1.1) translateY(0);
  background-color: #1ed760;
}

.playlist-info {
  padding: 1.5rem;
}

.playlist-name {
  font-size: 1.3rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.playlist-meta {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.playlist-tracks,
.playlist-id {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--spotify-light-gray);
  font-size: 0.85rem;
}

.id-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}

.playlist-actions {
  padding-top: 0.75rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.spotify-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--spotify-white);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.2s ease;
}

.spotify-link:hover {
  color: var(--spotify-green);
}

/* Responsive Styles */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .playlist-stats {
    width: 100%;
    justify-content: flex-start;
  }
  
  .playlists-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 1.5rem;
  }
}

@media (max-width: 480px) {
  .playlists-grid {
    grid-template-columns: 1fr;
  }
}
</style>