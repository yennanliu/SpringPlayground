<template>
  <div>
    <form>
      <div class="form-group">
        <label>Album ID</label>
        <input type="text" class="form-control" v-model="albumId" required />
      </div>
      <button type="button" class="btn btn-primary" @click="fetchAlbum">
        Submit
      </button>
    </form>

    <h1 v-if="album">
      Album: {{ album.name }} | Artist: {{ album.artists[0].name }}
    </h1>
    <div v-if="album">
      <p>
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
      />
      <div v-for="track in album.tracks.items" :key="track.id">
        <p>Track: {{ track.name }}</p>
        <p>
          URL:
          <a :href="track.externalUrls.externalUrls.spotify" target="_blank">{{
            track.externalUrls.externalUrls.spotify
          }}</a>
        </p>
        <hr />

        <p>
          Preview URL:
          <audio controls>
            <source :src="track.previewUrl" type="audio/mpeg" />
            Your browser does not support the audio element.
          </audio>
        </p>

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
      album: null,
    };
  },
  mounted() {
    //this.fetchAlbum();
  },
  methods: {
    async fetchAlbum() {
      try {
        const response = await fetch(
          //"http://localhost:8888/album/5zT1JLIj9E57p3e1rFm9Uq"
          `http://localhost:8888/album/${this.albumId}`
        );
        if (!response.ok) {
          throw new Error("Failed to fetch album");
        }
        const data = await response.json();
        this.album = data;
        console.log("this.album = {}", JSON.stringify(this.album));
        // console.log("external url = {}", JSON.stringify(this.album.externalUrls.spotify))
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
