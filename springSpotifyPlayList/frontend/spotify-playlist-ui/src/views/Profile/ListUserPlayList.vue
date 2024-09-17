<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1 class="main-heading">User Playlists</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <div
        v-for="playList in playLists"
        :key="playList.id"
        class="col-md-6 col-xl-4 col-12 pt-4"
      >
        <div class="card playlist-card shadow-lg">
          <img
            v-if="playList.images && playList.images.length > 0"
            :src="playList.images[0].url"
            class="card-img-top playlist-image"
            :alt="playList.name"
          />
          <div class="card-body">
            <h3 class="card-title playlist-name">{{ playList.name }}</h3>
            <p class="card-text playlist-id">ID: {{ playList.id }}</p>
            <a
              :href="playList.externalUrls.externalUrls.spotify"
              class="btn btn-outline-light playlist-btn"
              target="_blank"
            >
              View on Spotify
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
  name: "ListUserPlayList",
  data() {
    return {
      playLists: [],
      msg: null,
    };
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get("http://localhost:8888/user_data/playlist/");
        this.playLists = response.data;
        console.log(">>> (fetchData) this.playLists =", JSON.stringify(this.playLists));
      } catch (error) {
        console.error(error);
        this.msg = "Failed to fetch playlists";
      }
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
/* Main container and heading */
.main-heading {
  font-family: "Roboto", sans-serif;
  color: #1db954; /* Spotify Green */
  font-weight: 700;
  font-size: 2.5rem;
  margin-top: 20px;
}

/* Playlist card styling */
.card {
  width: 100%;
  border-radius: 15px;
  overflow: hidden;
  background-color: #191414; /* Spotify dark theme */
}

.playlist-card {
  transition: transform 0.3s ease-in-out;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
}

.playlist-card:hover {
  transform: scale(1.05);
}

/* Playlist image */
.playlist-image {
  max-height: 350px;
  object-fit: cover;
  border-bottom: 4px solid #1db954;
}

/* Playlist name and ID */
.playlist-name {
  font-size: 1.75rem;
  font-weight: 700;
  color: #fff;
}

.playlist-id {
  font-size: 1rem;
  color: #b3b3b3;
}

/* Button styling */
.playlist-btn {
  font-size: 1rem;
  color: #1db954;
  border-color: #1db954;
  font-weight: bold;
  margin-top: 15px;
  transition: background-color 0.3s, color 0.3s;
}

.playlist-btn:hover {
  background-color: #1db954;
  color: #191414;
}
</style>