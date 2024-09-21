<template>
  <div id="home">
    <div class="home-container">
      <div class="home-banner">
        <h1>Welcome to Spotify Playlist Manager</h1>
        <p>Manage and explore your Spotify playlists with ease.</p>
        <router-link to="/profile" class="btn btn-primary"
          >View Your Playlists</router-link
        >
      </div>
      <div class="features-section">
        <div class="feature">
          <h2>Browse Playlists</h2>
          <p>
            Access your personal
            <router-link to="/profile" class="btn btn-primary"
              >playlists</router-link
            >
            directly from Spotify.
          </p>
        </div>
        <div class="feature">
          <h2>Search Albums</h2>
          <p>
            Find
            <router-link to="/search_album" class="btn btn-primary"
              >albums</router-link
            >, explore tracks, and listen to previews.
          </p>
        </div>
        <div class="feature">
          <h2>Personalize Listening</h2>
          <p>
            Song
            <router-link to="/recommendation" class="btn btn-primary"
              >recommendations</router-link
            >
            with ML, preference, create your own playList!
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
.home-container {
  text-align: center;
  padding: 50px;
}

.home-banner {
  background-color: #1db954;
  color: white;
  padding: 100px 0;
}

.home-banner h1 {
  font-size: 3rem;
  font-family: "Roboto", sans-serif;
}

.home-banner p {
  font-size: 1.25rem;
  margin: 20px 0;
}

.features-section {
  display: flex;
  justify-content: space-around;
  padding: 50px 0;
}

.feature {
  width: 30%;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
}

.feature h2 {
  font-size: 1.75rem;
  font-family: "Roboto", sans-serif;
}

.feature p {
  font-size: 1rem;
  color: #333;
}
</style>