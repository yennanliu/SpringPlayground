<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/album">Album</router-link> |
      <router-link to="/playlist">Playlist</router-link> |
      <router-link to="/profile">Profile</router-link> |
      <router-link to="/search_album">Search Album</router-link> |
      <router-link to="/search_artist">Search Artist</router-link> |
      <router-link to="/recommendation">Recommendation</router-link>
    </nav>
    <div class="auth-button">
      <button @click="authorize" class="btn btn-outline-light">
        Authorize with Spotify
      </button>
    </div>
    <router-view />
  </div>
</template>

<script>
export default {
  methods: {
    async authorize() {
      try {
        const response = await fetch("http://localhost:8888/authorize");
        if (!response.ok) {
          throw new Error("Failed to authorize with Spotify");
        }
        const data = await response.json();
        if (data.url) {
          window.location.href = data.url;
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
