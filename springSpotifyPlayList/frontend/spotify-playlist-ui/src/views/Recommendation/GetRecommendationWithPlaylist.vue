<template>
  <div class="container">
    <h1>Song Recommend via PlayList</h1>
    <form
      @submit.prevent="getRecommendWithPlayList"
      class="recommendation-form"
    >
      <!-- Playlist ID for feature recommendation -->
      <div class="form-group">
        <label>Feature Playlist ID</label>
        <input
          type="text"
          class="form-control large-input"
          v-model="playlistFeatureId"
          placeholder="Playlist ID feature"
        />
      </div>

      <!-- Playlist ID Input for adding new songs -->
      <div class="form-group">
        <label>To add Playlist ID</label>
        <input
          type="text"
          class="form-control large-input"
          v-model="playlistId"
          placeholder="Playlist IDss adding new songs"
        />
      </div>

      <div class="button-group">
        <button type="submit" class="btn btn-outline-light">Get Recommend</button>
        <button
          type="button"
          class="btn btn-outline-light"
          @click="addSongToPlayList"
        >
          Add to Playlist
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

        <img
          v-if="track.album.images && track.album.images.length > 0"
          :src="track.album.images[0].url"
          :alt="track.name"
          class="album-img"
        />

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
      tracks: null,
      trackURIs: "",
      playlistFeatureId: "",
      playlistId: "",
      addToPlayList: false,
    };
  },
  methods: {
    async getRecommendWithPlayList() {
      try {
        //sthis.playlistFeatureId = "1VxF9hsEnBWM1CAXjzecMU"
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