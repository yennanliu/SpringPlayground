<template>
  <div>
    <form>
      <div class="form-group">
        <label>Recommendation</label>
        <input type="text" class="form-control" v-model="albumId" required />
      </div>
      <button type="button" class="btn btn-primary" @click="getRecommend">
        Submit
      </button>
    </form>

    <!-- <h1 v-if="tracks">
      Album: {{ album.name }} | Artist: {{ album.artists[0].name }}
    </h1> -->
    <div v-if="tracks">
      <!-- <p>
        Album URL:
        <a :href="album.externalUrls.externalUrls.spotify" target="_blank">{{
          album.externalUrls.externalUrls.spotify
        }}</a>
      </p>
      <img
        v-if="album.images && album.images.length > 0"
        :src="album.images[0].url"
        :alt="album.name"
        style="max-width: 300px; max-height: 300px"
      /> -->
      <div v-for="track in tracks.tracks" :key="track.id">
        <p>Track: {{ track.name }} | Artist: {{ track.artists[0].name }}</p>
        <p>
          URL:
          <a :href="track.externalUrls.externalUrls.spotify" target="_blank">{{
            track.externalUrls.externalUrls.spotify
          }}</a>
        </p>

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
  data() {
    return {
      albumId: null,
      tracks: null,
    };
  },
  mounted() {},
  methods: {
    async getRecommend() {
      try {
        const response = await fetch(
          //"http://localhost:8888/album/5zT1JLIj9E57p3e1rFm9Uq"
          //`http://localhost:8888/album/${this.albumId}`
          "http://localhost:8888/recommend/"
        );
        if (!response.ok) {
          throw new Error("Failed to fetch album");
        }
        const data = await response.json();
        this.tracks = data;
        console.log("this.tracks = {}", JSON.stringify(this.tracks));
        // console.log("external url = {}", JSON.stringify(this.album.externalUrls.spotify))
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
  