<template>
  <div class="recommendation-page">
    <div class="page-header">
      <h1>Song Recommendations</h1>
      <p class="page-subtitle">Get personalized recommendations based on your preferences</p>
    </div>

    <div class="content-container">
      <div class="form-section">
        <form @submit.prevent="getRecommend" class="recommendation-form">
          <div class="form-header">
            <i class="fas fa-sliders-h"></i>
            <h2>Recommendation Parameters</h2>
          </div>
          
          <!-- Amount Input -->
          <div class="form-group">
            <label for="amount">
              <i class="fas fa-sort-numeric-up"></i> Number of Tracks
            </label>
            <input
              type="number"
              id="amount"
              v-model="amount"
              min="1"
              max="50"
              required
            />
          </div>

          <!-- Market Dropdown -->
          <div class="form-group">
            <label for="market">
              <i class="fas fa-globe-americas"></i> Market
            </label>
            <select id="market" v-model="market" required>
              <option value="JP">Japan (JP)</option>
              <option value="TW">Taiwan (TW)</option>
              <option value="US">United States (US)</option>
              <option value="UK">United Kingdom (UK)</option>
              <option value="FR">France (FR)</option>
              <option value="TH">Thailand (TH)</option>
            </select>
          </div>

          <!-- Seed Artist ID Input -->
          <div class="form-group">
            <label for="seedArtistId">
              <i class="fas fa-user-circle"></i> Seed Artist ID
            </label>
            <input
              type="text"
              id="seedArtistId"
              v-model="seedArtistId"
              placeholder="Example: 4sJCsXNYmUMeumUKVz4Abm"
              required
            />
          </div>

          <!-- Genres Dropdown -->
          <div class="form-group">
            <label for="seedGenres">
              <i class="fas fa-guitar"></i> Genre
            </label>
            <select id="seedGenres" v-model="seedGenres" required>
              <option value="rock">Rock</option>
              <option value="electro">Electro</option>
              <option value="hip-hop">Hip-Hop</option>
              <option value="j-pop">J-Pop</option>
              <option value="k-pop">K-Pop</option>
              <option value="soul">Soul</option>
              <option value="house">House</option>
              <option value="jazz">Jazz</option>
            </select>
          </div>

          <!-- Seed Track Input -->
          <div class="form-group">
            <label for="seedTrack">
              <i class="fas fa-music"></i> Seed Track ID
            </label>
            <input
              type="text"
              id="seedTrack"
              v-model="seedTrack"
              placeholder="Example: 1ZFQgnAwHaAhAn1o2bkwVs"
              required
            />
          </div>

          <!-- Popularity Range Controls -->
          <div class="popularity-controls">
            <div class="slider-group">
              <label for="minPopularity">
                <i class="fas fa-star-half-alt"></i> Min Popularity: {{ minPopularity }}
              </label>
              <input
                type="range"
                id="minPopularity"
                min="0"
                max="100"
                v-model="minPopularity"
                class="slider"
              />
            </div>
            
            <div class="slider-group">
              <label for="maxPopularity">
                <i class="fas fa-star"></i> Max Popularity: {{ maxPopularity }}
              </label>
              <input
                type="range"
                id="maxPopularity"
                min="0"
                max="100"
                v-model="maxPopularity"
                class="slider"
              />
            </div>
            
            <div class="slider-group">
              <label for="targetPopularity">
                <i class="fas fa-bullseye"></i> Target Popularity: {{ targetPopularity }}
              </label>
              <input
                type="range"
                id="targetPopularity"
                min="0"
                max="100"
                v-model="targetPopularity"
                class="slider"
              />
            </div>
          </div>

          <!-- Playlist ID Input -->
          <div class="form-group">
            <label for="playlistId">
              <i class="fas fa-list"></i> Target Playlist ID (Optional)
            </label>
            <input
              type="text"
              id="playlistId"
              v-model="playlistId"
              placeholder="Add recommendations to this playlist"
            />
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
      </div>

      <!-- Results Section -->
      <div class="results-section">
        <!-- Loading Indicator -->
        <div v-if="isLoading" class="loading-container">
          <div class="spinner"></div>
          <p>Finding the perfect tracks for you...</p>
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
          <p>No recommendations found with these parameters</p>
          <p class="suggestion">Try adjusting your preferences and search again</p>
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
      amount: 10,
      market: "JP",
      maxPopularity: 100,
      minPopularity: 0,
      seedArtistId: "4sJCsXNYmUMeumUKVz4Abm",
      seedGenres: "electro",
      seedTrack: "1ZFQgnAwHaAhAn1o2bkwVs",
      targetPopularity: 50,
      tracks: null,
      trackURIs: "",
      playlistId: "",
      addToPlayList: false,
      isLoading: false,
      getRecommendError: null,
    };
  },
  methods: {
    async getRecommend() {
      this.isLoading = true;
      this.addToPlayList = false;
      this.getRecommendError = null;
      
      try {
        const baseUrl = this.baseURL.endsWith('/') ? this.baseURL.slice(0, -1) : this.baseURL;
        const response = await fetch(`${baseUrl}/recommend`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            amount: this.amount,
            market: this.market,
            maxPopularity: this.maxPopularity,
            minPopularity: this.minPopularity,
            seedArtistId: this.seedArtistId,
            seedGenres: this.seedGenres,
            seedTrack: this.seedTrack,
            targetPopularity: this.targetPopularity,
          }),
        });

        if (response.status === 200) {
          const data = await response.json();
          this.tracks = data;
        } 
        else {
          throw new Error("Failed to fetch recommendations");
        }
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
  margin-bottom: 1.25rem;
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

.form-group input[type="text"],
.form-group input[type="number"],
.form-group select {
  width: 100%;
  padding: 0.75rem;
  background-color: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  color: var(--spotify-white);
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(29, 185, 84, 0.5);
  background-color: rgba(255, 255, 255, 0.15);
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

/* Slider Controls */
.popularity-controls {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
}

.slider-group {
  margin-bottom: 1.25rem;
}

.slider-group:last-child {
  margin-bottom: 0;
}

.slider-group label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
  font-weight: 600;
  color: var(--spotify-white);
  font-size: 0.95rem;
}

.slider-group label i {
  color: var(--spotify-green);
  width: 20px;
  text-align: center;
}

.slider {
  -webkit-appearance: none;
  width: 100%;
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  outline: none;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--spotify-green);
  cursor: pointer;
  transition: all 0.2s ease;
}

.slider::-webkit-slider-thumb:hover {
  transform: scale(1.2);
}

.slider::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--spotify-green);
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
}

.slider::-moz-range-thumb:hover {
  transform: scale(1.2);
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