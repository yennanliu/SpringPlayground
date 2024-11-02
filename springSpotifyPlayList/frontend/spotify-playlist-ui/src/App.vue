<template>
  <div id="app">
    <header>
      <nav>
        <router-link to="/">Home</router-link> |

        <!-- Dropdown for User profile with arrow indicator -->
        <div class="dropdown">
          <span class="dropdown-label">User Profile ▼</span>
          <div class="dropdown-content">
            <router-link to="/profile">User PlaytList</router-link>
          </div>
        </div>
        |

        <!-- Dropdown for Search options with arrow indicator -->
        <div class="dropdown">
          <span class="dropdown-label">Search ▼</span>
          <div class="dropdown-content">
            <router-link to="/search_album">Search Album</router-link>
            <router-link to="/search_artist">Search Artist</router-link>
            <router-link to="/album">Search Album (by ID)</router-link>
          </div>
        </div>
        |

        <router-link to="/playlist">Create Playlist</router-link> |

        <!-- Dropdown for Recommendations options with arrow indicator -->
        <div class="dropdown">
          <span class="dropdown-label">Recommendation ▼</span>
          <div class="dropdown-content">
            <router-link to="/recommendation">Recommendation</router-link>
            <router-link to="/recommendationWithPlayList"
              >Recommendation With Playlist</router-link
            >
          </div>
        </div>
      </nav>
    </header>

    <div class="auth-status">
      <!-- Show ✅ or ❌ based on authorization status -->
      <p>
        <span v-if="isAuthorized">✅ Authorized</span>
        <span v-else>❌ Not Authorized</span>
      </p>
      <!-- Always show the authorize button -->
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
      isAuthorized: false,
    };
  },
  methods: {
    checkAuthorization() {
      // Check localStorage for authorization status
      this.isAuthorized = localStorage.getItem("spotifyAuthorized") === "true";
    },
    async authorize() {
      try {
        const response = await fetch(`${this.baseURL}/authorize`);
        if (!response.ok) {
          throw new Error("Failed to authorize with Spotify");
        }
        const data = await response.json();
        if (data.url) {
          // Set localStorage flag before redirecting
          localStorage.setItem("spotifyAuthorized", "true");
          window.location.href = data.url;
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
      }
    },
  },
  mounted() {
    this.checkAuthorization();
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

.auth-status {
  margin-top: 20px;
}

.auth-status .btn {
  font-size: 1.2rem;
  padding: 10px 20px;
}

/* Dropdown Styling */
.dropdown {
  display: inline-block;
  position: relative;
}

.dropdown-label {
  cursor: pointer;
  color: #fff;
  text-decoration: none;
  padding: 0 15px;
  display: inline-block;
}

.dropdown-label:hover {
  color: #ffeb3b;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #fff;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1;
  min-width: 150px;
  border-radius: 5px;
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