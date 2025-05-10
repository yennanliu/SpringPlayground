<template>
  <div class="search-page">
    <div class="search-header">
      <h1>Album Search</h1>
      <p class="search-subtitle">Find albums by keyword and explore their tracks</p>
    </div>

    <!-- Search Form -->
    <div class="search-form-container">
      <div class="search-input-group">
        <i class="fas fa-search search-icon"></i>
        <input
          type="text"
          v-model="albumKeyword"
          placeholder="Enter album keyword..."
          class="search-input"
          @keyup.enter="searchAlbum"
        />
        <button
          type="button"
          class="search-button"
          @click="searchAlbum"
        >
          Search
        </button>
      </div>
    </div>

    <!-- Loading Indicator -->
    <div v-if="isLoading" class="loading-container">
      <div class="spinner"></div>
      <p>Searching for albums...</p>
    </div>

    <!-- Search Results -->
    <div v-else-if="albums.length > 0" class="search-results">
      <div class="results-header">
        <h2>
          <span class="result-count">{{ albums.length }}</span> Albums Found
        </h2>
      </div>
      
      <div class="album-grid">
        <div v-for="album in albums" :key="album.id" class="album-card">
          <!-- Album Cover Image -->
          <div class="album-image-container">
            <img
              v-if="album.images && album.images.length > 0"
              :src="album.images[0].url"
              :alt="album.name"
              class="album-cover"
            />
            <div v-else class="album-placeholder">
              <i class="fas fa-music"></i>
            </div>
          </div>
          
          <div class="album-info">
            <h3 class="album-title">{{ album.name }}</h3>
            <p class="album-artist">{{ album.artists[0].name }}</p>
            
            <div class="album-details">
              <span class="album-id">
                <i class="fas fa-fingerprint"></i> {{ album.id }}
              </span>
            </div>
            
            <a 
              v-if="album.external_urls && album.external_urls.spotify" 
              :href="album.external_urls.spotify" 
              target="_blank" 
              class="spotify-link"
            >
              <i class="fab fa-spotify"></i> Open in Spotify
            </a>
          </div>
          
          <!-- Track List (Expandable) -->
          <div class="track-list-container">
            <button class="toggle-tracks-btn" @click="toggleTracks(album.id)">
              <i class="fas fa-list"></i> 
              {{ expandedAlbum === album.id ? 'Hide Tracks' : 'Show Tracks' }}
            </button>
            
            <div v-if="expandedAlbum === album.id" class="track-list">
              <div v-if="album.tracks && album.tracks.length > 0">
                <div 
                  v-for="(track, index) in album.tracks" 
                  :key="track.id" 
                  class="track-item"
                  :class="{'even-row': index % 2 === 0}"
                >
                  <div class="track-number">{{ index + 1 }}</div>
                  <div class="track-info">
                    <div class="track-name">{{ track.name }}</div>
                    <div class="track-actions">
                      <a 
                        v-if="track.external_urls && track.external_urls.spotify" 
                        :href="track.external_urls.spotify" 
                        target="_blank"
                        class="track-link"
                      >
                        <i class="fab fa-spotify"></i>
                      </a>
                      <div v-if="track.preview_url" class="audio-player">
                        <audio controls>
                          <source :src="track.preview_url" type="audio/mpeg" />
                        </audio>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <p v-else class="no-tracks">No tracks available for this album</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- No Results Found -->
    <div v-else-if="hasSearched" class="no-results">
      <i class="fas fa-search"></i>
      <p>No albums found for "{{ albumKeyword }}"</p>
      <p class="suggestion">Try a different keyword or check your spelling</p>
    </div>
  </div>
</template>
  
<script>
export default {
  props: ["baseURL"],
  data() {
    return {
      albumKeyword: "funky", // default value
      albums: [],
      isLoading: false,
      expandedAlbum: null, // To track which album's tracks are expanded
      hasSearched: false, // To track if search has been performed
    };
  },
  methods: {
    async searchAlbum() {
      if (!this.albumKeyword.trim()) return;
      
      this.isLoading = true;
      this.albums = [];
      this.expandedAlbum = null;
      
      try {
        console.log("this.albumKeyword = " + this.albumKeyword);
        const response = await fetch(
          `${this.baseURL}/search/album/?keyword=${this.albumKeyword.trim()}`
        );
        
        if (!response.ok) {
          throw new Error("Failed to search album");
        }
        
        const data = await response.json();
        this.albums = data;
        this.hasSearched = true;
        
        console.log("this.albums length = ", this.albums.length);
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },
    
    toggleTracks(albumId) {
      this.expandedAlbum = this.expandedAlbum === albumId ? null : albumId;
    }
  },
};
</script>
  
<style scoped>
/* Page Layout */
.search-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 0;
}

.search-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.search-header h1 {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.search-subtitle {
  color: var(--spotify-light-gray);
  font-size: 1.1rem;
}

/* Search Form */
.search-form-container {
  max-width: 700px;
  margin: 0 auto 3rem;
}

.search-input-group {
  position: relative;
  display: flex;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border-radius: 50px;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.5rem;
}

.search-icon {
  position: absolute;
  left: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--spotify-light-gray);
  font-size: 1.2rem;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 1rem 1rem 1rem 3.5rem;
  font-size: 1.1rem;
  color: var(--spotify-white);
  border-radius: 50px;
}

.search-input:focus {
  outline: none;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.search-button {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  border: none;
  border-radius: 50px;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-button:hover {
  background-color: #1ed760;
  transform: translateY(-2px);
}

/* Loading Indicator */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-left-color: var(--spotify-green);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-container p {
  color: var(--spotify-light-gray);
}

/* Search Results */
.search-results {
  margin-top: 2rem;
}

.results-header {
  margin-bottom: 2rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.results-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--spotify-white);
}

.result-count {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 700;
  margin-right: 0.5rem;
}

/* Album Grid */
.album-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.album-card {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100%;
}

.album-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.album-image-container {
  position: relative;
  aspect-ratio: 1 / 1;
  overflow: hidden;
}

.album-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.album-card:hover .album-cover {
  transform: scale(1.05);
}

.album-placeholder {
  width: 100%;
  height: 100%;
  background-color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
}

.album-placeholder i {
  font-size: 3rem;
  color: #555;
}

.album-info {
  padding: 1.5rem;
}

.album-title {
  font-size: 1.4rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.album-artist {
  color: var(--spotify-green);
  font-weight: 600;
  margin-bottom: 1rem;
}

.album-details {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  color: var(--spotify-light-gray);
  font-size: 0.85rem;
}

.album-id {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.spotify-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--spotify-white);
  text-decoration: none;
  font-size: 0.9rem;
  padding: 0.5rem 0;
  transition: color 0.2s ease;
}

.spotify-link:hover {
  color: var(--spotify-green);
}

/* Track List */
.track-list-container {
  padding: 0 1.5rem 1.5rem;
}

.toggle-tracks-btn {
  background: none;
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: var(--spotify-white);
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
}

.toggle-tracks-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: var(--spotify-green);
}

.track-list {
  margin-top: 1rem;
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  overflow: hidden;
  max-height: 300px;
  overflow-y: auto;
}

.track-item {
  display: flex;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.track-item.even-row {
  background-color: rgba(255, 255, 255, 0.02);
}

.track-number {
  width: 30px;
  color: var(--spotify-light-gray);
  font-size: 0.9rem;
  padding-top: 0.25rem;
}

.track-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.track-name {
  font-size: 0.95rem;
  font-weight: 500;
}

.track-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.track-link {
  color: var(--spotify-light-gray);
  transition: color 0.2s ease;
}

.track-link:hover {
  color: var(--spotify-green);
}

.audio-player {
  max-width: 150px;
}

.audio-player audio {
  height: 30px;
}

.no-tracks {
  padding: 1rem;
  text-align: center;
  color: var(--spotify-light-gray);
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
@media (max-width: 768px) {
  .search-page {
    padding: 1rem;
  }
  
  .album-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1.5rem;
  }
}

@media (max-width: 576px) {
  .search-input-group {
    flex-direction: column;
    border-radius: 12px;
    padding: 0.75rem;
  }
  
  .search-icon {
    top: 1.75rem;
  }
  
  .search-input {
    margin-bottom: 0.75rem;
  }
  
  .search-button {
    width: 100%;
  }
  
  .album-grid {
    grid-template-columns: 1fr;
  }
}
</style>