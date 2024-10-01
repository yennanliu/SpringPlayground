<template>
  <div id="app">
    <header>
      <nav>
        <router-link to="/">Home</router-link> |
        <router-link to="/profile">User Profile</router-link> |
        <router-link to="/album">Get Album (Id)</router-link> |
        <router-link to="/search_album">Get Albums</router-link> |
        <router-link to="/search_artist">Get Artists</router-link> |
        <router-link to="/playlist">Create Playlist</router-link> |
        <router-link to="/recommendation">Recommendation</router-link>
      </nav>
    </header>
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
  data() {
    return {
      baseURL: "http://localhost:8888/",
    };
  },
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

header {
  background-color: #022917;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

nav {
  font-size: 0.7rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 2px;
}

nav a {
  color: #fff;
  margin: 0 15px;
  text-decoration: none;
  transition: color 0.3s ease;
}

nav a:hover {
  color: #000;
}

nav a.router-link-exact-active {
  color: #ffeb3b;
}

.auth-button {
  margin-top: 20px;
}

.auth-button .btn {
  font-size: 1.2rem;
  padding: 10px 20px;
}
</style>