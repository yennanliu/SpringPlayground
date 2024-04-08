<template>
  <div>
    <form>
      <div class="form-group">
        <label>Amount</label>
        <input type="number" class="form-control" v-model="amount" required />
      </div>
      <div class="form-group">
        <label>Market</label>
        <input type="text" class="form-control" v-model="market" required />
      </div>
      <div class="form-group">
        <label>Max Popularity</label>
        <input
          type="number"
          class="form-control"
          v-model="maxPopularity"
          required
        />
      </div>
      <div class="form-group">
        <label>Min Popularity</label>
        <input
          type="number"
          class="form-control"
          v-model="minPopularity"
          required
        />
      </div>
      <div class="form-group">
        <label>Seed Artist ID</label>
        <input
          type="text"
          class="form-control"
          v-model="seedArtistId"
          required
        />
      </div>
      <div class="form-group">
        <label>Seed Genres</label>
        <input type="text" class="form-control" v-model="seedGenres" required />
      </div>
      <div class="form-group">
        <label>Seed Track</label>
        <input type="text" class="form-control" v-model="seedTrack" required />
      </div>
      <div class="form-group">
        <label>Target Popularity</label>
        <input
          type="number"
          class="form-control"
          v-model="targetPopularity"
          required
        />
      </div>
      <button type="button" class="btn btn-primary" @click="getRecommend">
        Submit
      </button>
    </form>

    <div v-if="tracks">
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
      amount: 10,
      market: "JP",
      maxPopularity: 100,
      minPopularity: 0,
      seedArtistId: "",
      seedGenres: "",
      seedTrack: "",
      targetPopularity: 50,
      tracks: null,
    };
  },
  methods: {
    async getRecommend() {
      try {
        const response = await fetch(
          // `http://localhost:8888/recommend?amount=${this.amount}&market=${this.market}&maxPopularity=${this.maxPopularity}&minPopularity=${this.minPopularity}&seedArtistId=${this.seedArtistId}&seedGenres=${this.seedGenres}&seedTrack=${this.seedTrack}&targetPopularity=${this.targetPopularity}`
          "http://localhost:8888/recommend/"
        );
        if (!response.ok) {
          throw new Error("Failed to fetch recommendations");
        }
        const data = await response.json();
        this.tracks = data;
        console.log("this.tracks = {}", JSON.stringify(this.tracks));
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>
