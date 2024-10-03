<template>
  <div class="container">
    <h1>Song Recommendation With ML</h1>
    <form @submit.prevent="getRecommend" class="recommendation-form">
      <!-- Amount Input -->
      <div class="form-group">
        <label>Amount</label>
        <input type="number" class="form-control large-input" v-model="amount" required />
      </div>

      <!-- Market Dropdown -->
      <div class="form-group">
        <label>Market</label>
        <select v-model="market" class="form-control large-input" required>
          <option value="JP">Japan (JP)</option>
          <option value="TW">Taiwan (TW)</option>
          <option value="US">United States (US)</option>
          <option value="UK">United Kingdom (UK)</option>
          <option value="FR">France (FR)</option>
          <option value="TH">Thailand (TH)</option>
        </select>
      </div>

      <!-- Max Popularity Slider -->
      <div class="form-group">
        <label>Max Popularity ({{ maxPopularity }})</label>
        <input type="range" min="0" max="100" v-model="maxPopularity" class="slider" />
      </div>

      <!-- Min Popularity Slider -->
      <div class="form-group">
        <label>Min Popularity ({{ minPopularity }})</label>
        <input type="range" min="0" max="100" v-model="minPopularity" class="slider" />
      </div>

      <!-- Seed Artist ID Input -->
      <div class="form-group">
        <label>Seed Artist ID</label>
        <input type="text" class="form-control large-input" v-model="seedArtistId" required />
      </div>

      <!-- Seed Genres Input -->
      <div class="form-group">
        <label>Seed Genres</label>
        <input type="text" class="form-control large-input" v-model="seedGenres" required />
      </div>

      <!-- Seed Track Input -->
      <div class="form-group">
        <label>Seed Track</label>
        <input type="text" class="form-control large-input" v-model="seedTrack" required />
      </div>

      <!-- Target Popularity Slider -->
      <div class="form-group">
        <label>Target Popularity ({{ targetPopularity }})</label>
        <input type="range" min="0" max="100" v-model="targetPopularity" class="slider" />
      </div>

      <!-- Playlist ID Input -->
      <div class="form-group">
        <label>Playlist ID</label>
        <input type="text" class="form-control large-input" v-model="playlistId" placeholder="Enter Playlist ID" />
      </div>

      <div class="button-group">
        <button type="submit" class="btn btn-outline-light">Submit</button>
        <button type="button" class="btn btn-outline-light" @click="addSongToPlayList">
          Add Songs to Playlist
        </button>
        <div v-if="addToPlayList">Songs added to Playlist successfully!</div>
      </div>
    </form>

    <!-- Display Tracks -->
    <div v-if="tracks">
      <div v-for="track in tracks.tracks" :key="track.id" class="track-card">
        <p>Track: {{ track.name }} | Artist: {{ track.artists[0].name }}</p>
        <p>
          URL:
          <a :href="track.externalUrls.externalUrls.spotify" target="_blank">{{
            track.externalUrls.externalUrls.spotify
          }}</a>
        </p>

        <img v-if="track.album.images && track.album.images.length > 0" :src="track.album.images[0].url"
          :alt="track.name" class="album-img" />

        <p>
          Preview URL:
          <audio controls>
            <source :src="track.previewUrl" type="audio/mpeg" />
            Your browser does not support the audio element.
          </audio>
        </p>
        <hr />
      </div>
    </div>
    <div v-else>Loading...</div>
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
      seedGenres: "electric",
      seedTrack: "1ZFQgnAwHaAhAn1o2bkwVs",
      targetPopularity: 50,
      tracks: null,
      trackURIs: "",
      playlistId: "",
      addToPlayList: false,
    };
  },
  methods: {
    async getRecommend() {
      try {
        const response = await fetch(`${this.baseURL}/recommend/`, {
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
        if (!response.ok) {
          throw new Error("Failed to fetch recommendations");
        }
        const data = await response.json();
        this.tracks = data;
      } catch (error) {
        console.error(error);
      }
    },

    async addSongToPlayList() {
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
        console.error(error);
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
}

.recommendation-form {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
}

.form-group {
  margin-bottom: 20px;
}

.large-input {
  font-size: 1.25rem;
  padding: 10px;
}

.slider {
  width: 100%;
  margin-top: 10px;
}

.button-group {
  display: flex;
  gap: 15px;
}

.track-card {
  margin-top: 20px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.album-img {
  max-width: 300px;
  max-height: 300px;
  margin-top: 10px;
}

.btn-outline-light {
  border-color: #1db954;
  color: #1db954;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 30px;
  text-transform: uppercase;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.btn-outline-light:hover {
  background-color: #1db954;
  color: #fff;
}

.button-group {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}
</style>