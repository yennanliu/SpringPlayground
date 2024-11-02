<template>
  <div id="app">
    <header>
      <nav>
        <router-link to="/">Home</router-link> |
        <router-link to="/profile">User Profile</router-link> |
        
        <!-- Dropdown for Search options -->
        <div class="dropdown">
          <span>Search</span>
          <div class="dropdown-content">
            <router-link to="/search_album">Get Albums</router-link>
            <router-link to="/search_artist">Get Artists</router-link>
            <router-link to="/album">Get Album (Id)</router-link>
          </div>
        </div> |

        <router-link to="/playlist">Create Playlist</router-link> |
        
        <!-- Dropdown for Recommendations options -->
        <div class="dropdown">
          <span>Recommendation</span>
          <div class="dropdown-content">
            <router-link to="/recommendation">Recommendation</router-link>
            <router-link to="/recommendationWithPlayList">Recommendation With Playlist</router-link>
          </div>
        </div>
      </nav>
    </header>

    <div class="auth-button">
      <button @click="authorize" class="btn btn-outline-light">
        Authorize with Spotify
      </button>
    </div>

    <router-view :baseURL="baseURL"></router-view>
  </div>
</template>

<script>
export default {
  data() {
    return {
      baseURL: process.env.VUE_APP_BASE_URL || "http://localhost:8888/",
    };
  },
  methods: {
    async authorize() {
      try {
        const response = await fetch(`${this.baseURL}/authorize`);
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

/* Dropdown Styling */
.dropdown {
  display: inline-block;
  position: relative;
}

.dropdown span {
  cursor: pointer;
  color: #fff;
  text-decoration: none;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #fff;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
  min-width: 150px;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-content a {
  color: #000;
  padding: 10px 15px;
  display: block;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}
</style>