<template>
  <div class="recommendation-page">
    <div class="page-header">
      <h1>Playlist-Based Recommendations</h1>
      <p class="page-subtitle">Get song recommendations based on your existing playlists</p>
    </div>

    <div class="content-container">
      <div class="form-section">
        <form @submit.prevent="getRecommendWithPlayList" class="recommendation-form">
          <div class="form-header">
            <i class="fas fa-list-alt"></i>
            <h2>Playlist Parameters</h2>
          </div>
          
          <!-- Feature Playlist ID Input -->
          <div class="form-group">
            <label for="playlistFeatureId">
              <i class="fas fa-music"></i> Source Playlist ID
            </label>
            <input
              type="text"
              id="playlistFeatureId"
              v-model="playlistFeatureId"
              placeholder="Playlist ID to base recommendations on"
              required
            />
            <small class="field-hint">The playlist whose features will be used for recommendations</small>
          </div>

          <!-- Target Playlist ID Input -->
          <div class="form-group">
            <label for="playlistId">
              <i class="fas fa-plus-circle"></i> Target Playlist ID (Optional)
            </label>
            <input
              type="text"
              id="playlistId"
              v-model="playlistId"
              placeholder="Playlist ID to add the recommended songs to"
            />
            <small class="field-hint">Where to add the recommended songs (leave empty to only view)</small>
          </div>

          <div class="form-actions">
            <button type="submit" class="action-button primary">
              <i class="fas fa-magic"></i> Get Recommendations
            </button>
            <button
              type="button"
              class="action-button secondary"
              @click="addSongToPlayList"
              :disabled="!tracks || !playlistId"
            >
              <i class="fas fa-plus-circle"></i> Add to Playlist
            </button>
          </div>
          
          <!-- Success Message -->
          <div v-if="addToPlayList" class="message success">
            <i class="fas fa-check-circle"></i>
            <span>Songs added to playlist successfully!</span>
          </div>
          
          <!-- Error Message -->
          <div v-if="getRecommendError" class="message error">
            <i class="fas fa-exclamation-circle"></i>
            <span>{{ getRecommendError }}</span>
          </div>
        </form>
        
        <div class="info-card">
          <div class="info-header">
            <i class="fas fa-lightbulb"></i>
            <h3>How It Works</h3>
          </div>
          <p>This feature analyzes a playlist you select and generates song recommendations with similar characteristics. It's a great way to expand your music collection based on playlists you already love.</p>
          <ol>
            <li>Enter a source playlist ID</li>
            <li>Get recommendations based on that playlist's musical style</li>
            <li>Optionally add the recommended songs to another playlist</li>
          </ol>
        </div>
      </div>

      <!-- Results Section -->
      <div class="results-section">
        <!-- Loading Indicator -->
        <div v-if="isLoading" class="loading-container">
          <div class="spinner"></div>
          <p>Analyzing playlist and finding similar tracks...</p>
        </div>
        
        <!-- Results -->
        <div v-else-if="tracks && tracks.tracks && tracks.tracks.length > 0" class="results-container">
          <div class="results-header">
            <h2><i class="fas fa-headphones-alt"></i> Recommended Tracks</h2>
            <span class="results-count">{{ tracks.tracks.length }} tracks</span>
          </div>
          
          <div class="tracks-grid">
            <div v-for="track in tracks.tracks" :key="track.id" class="track-card">
              <div class="track-image">
                <img
                  v-if="track.album.images && track.album.images.length > 0"
                  :src="track.album.images[0].url"
                  :alt="track.name"
                />
                <div v-else class="image-placeholder">
                  <i class="fas fa-music"></i>
                </div>
              </div>
              
              <div class="track-info">
                <h3 class="track-name">{{ track.name }}</h3>
                <p class="track-artist">{{ track.artists[0].name }}</p>
                
                <div class="track-preview" v-if="track.previewUrl">
                  <audio controls>
                    <source :src="track.previewUrl" type="audio/mpeg" />
                    Your browser does not support audio playback.
                  </audio>
                </div>
                <p v-else class="no-preview">No preview available</p>
                
                <a 
                  v-if="track.externalUrls && track.externalUrls.externalUrls && track.externalUrls.externalUrls.spotify" 
                  :href="track.externalUrls.externalUrls.spotify" 
                  target="_blank" 
                  class="spotify-link"
                >
                  <i class="fab fa-spotify"></i> Open in Spotify
                </a>
              </div>
            </div>
          </div>
        </div>
        
        <!-- No Results State -->
        <div v-else-if="tracks && (!tracks.tracks || tracks.tracks.length === 0)" class="no-results">
          <i class="fas fa-search"></i>
          <p>No recommendations found for this playlist</p>
          <p class="suggestion">Try a different playlist with more tracks</p>
        </div>
        
        <!-- Initial State -->
        <div v-else-if="!isLoading && !tracks" class="empty-state">
          <i class="fas fa-music"></i>
          <p>Enter a playlist ID and click "Get Recommendations" to begin</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["baseURL"],
  data() {
    return {
      tracks: null,
      trackURIs: "",
      playlistFeatureId: "",
      playlistId: "",
      addToPlayList: false,
      isLoading: false,
      getRecommendError: null,
    };
  },
  methods: {
    async getRecommendWithPlayList() {
      this.isLoading = true;
      this.addToPlayList = false;
      this.getRecommendError = null;
      
      try {
        console.log(">>> this.playlistFeatureId = " + this.playlistFeatureId);
        const response = await fetch(
          `${this.baseURL}/recommend/playlist/${this.playlistFeatureId}`,
          {
            method: "GET",
          }
        );
        if (!response.ok) {
          throw new Error("Failed to fetch recommendations");
        }
        const data = await response.json();
        this.tracks = data;
      } catch (error) {
        this.getRecommendError = error.message;
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },

    async addSongToPlayList() {
      if (!this.tracks || !this.playlistId) return;
      
      this.addToPlayList = false;
      
      try {
        this.trackURIs = this.tracks.tracks.map((track) => track.uri);

        const response = await fetch(`${this.baseURL}/playlist/addSong`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            songUris: this.trackURIs.toString(),
            playlistId: this.playlistId,
          }),
        });
        if (response.status === 200) {
          console.log("Songs added successfully");
          this.addToPlayList = true;
        } else {
          throw new Error("Failed to add songs to playlist");
        }
      } catch (error) {
        this.getRecommendError = error.message;
        console.error(error);
      }
    },
  },
};
</script>

<style scoped>
.recommendation-page {
  max-width: 1200px;
  margin: 0 auto;
  padding-bottom: 2rem;
}

.page-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.page-header h1 {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  color: var(--spotify-light-gray);
  font-size: 1.1rem;
}

.content-container {
  display: grid;
  grid-template-columns: minmax(300px, 400px) 1fr;
  gap: 2rem;
  align-items: start;
}

/* Form Section */
.form-section {
  position: sticky;
  top: 100px;
}

.recommendation-form {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  margin-bottom: 2rem;
}

.recommendation-form:before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 5px;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
}

.form-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.form-header i {
  font-size: 1.5rem;
  color: var(--spotify-green);
}

.form-header h2 {
  font-size: 1.4rem;
  font-weight: 700;
  margin: 0;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: var(--spotify-white);
  font-size: 0.95rem;
}

.form-group label i {
  color: var(--spotify-green);
  width: 20px;
  text-align: center;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  background-color: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  color: var(--spotify-white);
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(29, 185, 84, 0.5);
  background-color: rgba(255, 255, 255, 0.15);
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.field-hint {
  display: block;
  color: var(--spotify-light-gray);
  font-size: 0.85rem;
  margin-top: 0.5rem;
  font-style: italic;
}

/* Info Card */
.info-card {
  background: rgba(40, 40, 40, 0.3);
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 1rem;
}

.info-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.info-header i {
  color: var(--spotify-green);
  font-size: 1.25rem;
}

.info-header h3 {
  font-size: 1.1rem;
  margin: 0;
  font-weight: 700;
}

.info-card p {
  color: var(--spotify-light-gray);
  font-size: 0.9rem;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.info-card ol {
  color: var(--spotify-light-gray);
  font-size: 0.9rem;
  padding-left: 1.5rem;
  margin-bottom: 0;
}

.info-card li {
  margin-bottom: 0.5rem;
}

.info-card li:last-child {
  margin-bottom: 0;
}

/* Form Actions */
.form-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-top: 1.5rem;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.85rem 1rem;
  border-radius: 50px;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.action-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.action-button.primary {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
}

.action-button.primary:hover:not(:disabled) {
  background-color: #1ed760;
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.action-button.secondary {
  background-color: transparent;
  color: var(--spotify-white);
  border: 2px solid var(--spotify-green);
}

.action-button.secondary:hover:not(:disabled) {
  background-color: rgba(29, 185, 84, 0.1);
  transform: translateY(-2px);
}

/* Message Boxes */
.message {
  margin-top: 1rem;
  padding: 0.85rem;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  animation: slideIn 0.3s ease forwards;
}

.message.success {
  background-color: rgba(29, 185, 84, 0.1);
  border-left: 4px solid var(--spotify-green);
  color: var(--spotify-green);
}

.message.error {
  background-color: rgba(255, 81, 81, 0.1);
  border-left: 4px solid #ff5151;
  color: #ff5151;
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

/* Results Section */
.results-section {
  min-height: 400px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-left-color: var(--spotify-green);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-container p {
  color: var(--spotify-light-gray);
  font-size: 1.1rem;
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 8rem 2rem;
  color: var(--spotify-light-gray);
  text-align: center;
  background: rgba(40, 40, 40, 0.2);
  border-radius: 16px;
}

.empty-state i {
  font-size: 3.5rem;
  margin-bottom: 1.5rem;
  opacity: 0.5;
}

.empty-state p {
  font-size: 1.2rem;
}

/* Results Header */
.results-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.results-header h2 {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.4rem;
  margin: 0;
}

.results-header h2 i {
  color: var(--spotify-green);
}

.results-count {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 700;
}

/* Tracks Grid */
.tracks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.track-card {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.track-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.track-image {
  width: 100%;
  aspect-ratio: 1;
  overflow: hidden;
}

.track-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.track-card:hover .track-image img {
  transform: scale(1.05);
}

.image-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #333 0%, #222 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-placeholder i {
  font-size: 3rem;
  color: rgba(255, 255, 255, 0.2);
}

.track-info {
  padding: 1.25rem;
}

.track-name {
  font-size: 1.2rem;
  font-weight: 700;
  margin: 0 0 0.5rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.track-artist {
  color: var(--spotify-green);
  font-weight: 600;
  margin-bottom: 1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.track-preview {
  margin-bottom: 1rem;
}

.track-preview audio {
  width: 100%;
  height: 30px;
}

.no-preview {
  color: var(--spotify-light-gray);
  font-size: 0.9rem;
  margin-bottom: 1rem;
  font-style: italic;
}

.spotify-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--spotify-white);
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.2s ease;
}

.spotify-link:hover {
  color: var(--spotify-green);
}

/* No Results State */
.no-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4rem 0;
  color: var(--spotify-light-gray);
}

.no-results i {
  font-size: 3rem;
  margin-bottom: 1.5rem;
  opacity: 0.7;
}

.no-results p {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

.suggestion {
  font-size: 0.9rem;
  opacity: 0.7;
}

/* Responsive Adjustments */
@media (max-width: 1024px) {
  .content-container {
    grid-template-columns: 1fr;
  }
  
  .form-section {
    position: static;
    margin-bottom: 2rem;
  }
  
  .recommendation-form {
    max-width: 700px;
    margin: 0 auto 1.5rem;
  }
  
  .info-card {
    max-width: 700px;
    margin: 0 auto;
  }
  
  .form-actions {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .tracks-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 576px) {
  .tracks-grid {
    grid-template-columns: 1fr;
  }
}
</style>