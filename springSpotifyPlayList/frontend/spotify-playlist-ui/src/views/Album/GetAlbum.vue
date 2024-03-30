<template>
  <div>
    <h1>Album Details</h1>
    <pre v-if="album">{{ JSON.stringify(album, null, 2) }}</pre>
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
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
