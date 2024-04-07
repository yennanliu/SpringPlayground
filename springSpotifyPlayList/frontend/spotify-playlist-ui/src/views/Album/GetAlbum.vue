<template>
  <div>
    <h1>Album : {{ album.name }}</h1>
    <div v-if="album">
      <p>Album URL: <a :href="album.externalUrls.externalUrls.spotify " target="_blank">{{ album.externalUrls.externalUrls.spotify }}</a></p>
      <img :src="album.images[0].url" :alt="album.name" style="max-width: 300px; max-height: 300px;">
      <div v-for="track in album.tracks.items" :key="track.id">
        <p>Track: {{ track.name }}</p>
        <p>URL: <a :href="track.externalUrls.externalUrls.spotify " target="_blank">{{ track.externalUrls.externalUrls.spotify }}</a></p>
        <hr>
      </div>
    </div>
    <div v-else>Loading...</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      album: null,
    };
  },
  mounted() {
    this.fetchAlbum();
  },
  methods: {
    async fetchAlbum() {
      try {
        const response = await fetch('http://localhost:8888/album/5zT1JLIj9E57p3e1rFm9Uq');
        if (!response.ok) {
          throw new Error('Failed to fetch album');
        }
        const data = await response.json();
        this.album = data;
        console.log("this.album = {}", JSON.stringify(this.album))
        // console.log("external url = {}", JSON.stringify(this.album.externalUrls.spotify))
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
