<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>User Playlists</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <div
        v-for="playList in playLists"
        :key="playList.id"
        class="col-md-6 col-xl-4 col-12 pt-3"
      >
        <div class="card">
          <img
            v-if="playList.images && playList.images.length > 0"
            :src="playList.images[0].url"
            class="card-img-top"
            :alt="playList.name"
            style="max-height: 200px; object-fit: cover"
          />
          <div class="card-body">
            <h5 class="card-title">{{ playList.name }}</h5>
            <p class="card-text">ID: {{ playList.id }}</p>
            <a
              :href="playList.externalUrls.externalUrls.spotify"
              class="btn btn-primary"
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
  name: "ListPlaylists",
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
h1 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

h5 {
  font-family: "Roboto", sans-serif;
  color: #686868;
  font-weight: 300;
}

.card {
  width: 100%;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
}

.card-text {
  font-size: 1rem;
}
</style>