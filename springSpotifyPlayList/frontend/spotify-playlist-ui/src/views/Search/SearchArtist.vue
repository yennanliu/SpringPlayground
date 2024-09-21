<template>
    <div class="container">
      <!-- Artist Keyword Form -->
      <form class="album-form text-center">
        <div class="form-group">
          <label for="artistKeyword" class="form-label">Enter Artist keyword</label>
          <input
            type="text"
            class="form-control form-control-lg"
            v-model="artistKeyword"
            placeholder="Search keywords"
            required
          />
        </div>
        <button
          type="button"
          class="btn btn-outline-light btn-lg"
          @click="searchArtist"
        >
          Submit
        </button>
      </form>
  
      <!-- Album Details -->
      <div v-if="artists.length > 0" class="album-details mt-5">
        <h2 class="text-center">Search Results</h2>
        <div v-for="artist in artists" :key="artist.id" class="album-card mt-4">
          <h3 class="album-title">
            Artist: {{ artist.name }} | id : {{ artist.id }}
          </h3>
  
          <!-- Album Image -->
          <!-- <div class="text-center">
            <img
              v-if="album.images && album.images.length > 0"
              :src="album.images[0].url"
              class="album-cover"
              :alt="album.name"
            />
          </div> -->
  
          <!-- Album URL -->
          <!-- <p class="album-url text-center">
            Album URL:
            <a :href="album.external_urls.spotify" target="_blank">
              View on Spotify
            </a>
          </p> -->
  
          <!-- Track List -->
          <!-- <div
            v-if="album.tracks && album.tracks.length > 0"
            class="track-list mt-4"
          >
            <h4 class="text-center">Tracks</h4>
            <ul>
              <li v-for="track in album.tracks" :key="track.id">
                <strong>{{ track.name }}</strong>
                <a :href="track.external_urls.spotify" target="_blank">
                  Listen on Spotify
                </a>
                <audio v-if="track.preview_url" controls class="track-preview">
                  <source :src="track.preview_url" type="audio/mpeg" />
                  Your browser does not support the audio element.
                </audio>
              </li>
            </ul>
          </div> -->
        </div>
      </div>
  
      <!-- Loading or No Albums Found -->
      <div v-else class="text-center mt-5">
        <p v-if="isLoading">Loading...</p>
        <p v-else>No artist found. Please try a different keyword.</p>
      </div>
    </div>
  </template>
    
    <script>
  export default {
    data() {
      return {
        artistKeyword: null,
        artists: [], // Array to hold multiple artists
        isLoading: false,
      };
    },
    methods: {
      async searchArtist() {
        this.isLoading = true;
        this.artists = []; // Reset the artists array for each new search
        try {
          console.log("this.artistKeyword = " + this.artistKeyword);
          const response = await fetch(
            `http://localhost:8888/search/artist/?keyword=${this.artistKeyword}`
          );
          if (!response.ok) {
            throw new Error("Failed to search artist");
          }
          //   const data = await response.json();
          //   this.albums = data.albums; // Assume 'data.albums.items' holds an array of albums
          const data = await response.json();
          this.artists = data;
          console.log("this.artists length =  ", this.artists.length);
          console.log("this.artists =", JSON.stringify(this.artists));
        } catch (error) {
          console.error(error);
        } finally {
          this.isLoading = false;
        }
      },
    },
  };
  </script>
    
    <style scoped>
  /* Main container and form styling */
  .container {
    max-width: 900px;
    margin: 0 auto;
  }
  
  .album-form {
    margin-top: 30px;
  }
  
  .form-label {
    color: #fff;
    font-size: 1.5rem;
  }
  
  .form-control-lg {
    font-size: 1.2rem;
    border-radius: 10px;
    padding: 10px;
  }
  
  .album-details {
    margin-top: 30px;
  }
  
  .album-card {
    background-color: #191414;
    border-radius: 15px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
  }
  
  .album-title {
    font-family: "Roboto", sans-serif;
    color: #1db954;
    font-weight: 700;
    font-size: 1.8rem;
    text-align: center;
    margin-bottom: 10px;
  }
  
  /* Album image styling */
  .album-cover {
    max-width: 400px;
    max-height: 400px;
    object-fit: cover;
    border-radius: 15px;
    border: 4px solid #1db954;
    margin-top: 10px;
  }
  
  /* URL and button styling */
  .album-url a {
    font-size: 1.2rem;
    color: #1db954;
    text-decoration: none;
  }
  
  .btn-outline-light {
    border-color: #1db954;
    color: #1db954;
    font-size: 1.2rem;
    font-weight: bold;
  }
  
  .btn-outline-light:hover {
    background-color: #1db954;
    color: #191414;
  }
  
  /* Track list styling */
  .track-list ul {
    list-style: none;
    padding: 0;
  }
  
  .track-list li {
    margin-bottom: 10px;
    font-size: 1.2rem;
    color: #fff;
  }
  
  .track-preview {
    margin-top: 5px;
  }
  
  .loading-text {
    font-size: 1.5rem;
    color: #686868;
  }
  </style>