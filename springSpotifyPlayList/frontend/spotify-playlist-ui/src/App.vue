<template>
  <div id="app">
    <header>
      <div class="navbar">
        <div class="logo">
          <router-link to="/">
            <span class="spotify-text">Spotify</span> Playlist Manager
          </router-link>
        </div>
        <nav>
          <div class="nav-links">
            <router-link to="/" class="nav-item">
              <i class="fas fa-home"></i> Home
            </router-link>
            
            <div class="dropdown">
              <span class="dropdown-label nav-item">
                <i class="fas fa-user"></i> Profile <i class="fas fa-chevron-down"></i>
              </span>
              <div class="dropdown-content">
                <router-link to="/profile">Your Playlists</router-link>
              </div>
            </div>
            
            <div class="dropdown">
              <span class="dropdown-label nav-item">
                <i class="fas fa-plus-circle"></i> Create <i class="fas fa-chevron-down"></i>
              </span>
              <div class="dropdown-content">
                <router-link to="/playlist">Create Playlist</router-link>
              </div>
            </div>

            <div class="dropdown">
              <span class="dropdown-label nav-item">
                <i class="fas fa-search"></i> Search <i class="fas fa-chevron-down"></i>
              </span>
              <div class="dropdown-content">
                <router-link to="/search_album">Search Album</router-link>
                <router-link to="/search_artist">Search Artist</router-link>
                <router-link to="/album">Search by Album ID</router-link>
              </div>
            </div>

            <div class="dropdown">
              <span class="dropdown-label nav-item">
                <i class="fas fa-music"></i> Recommendation <i class="fas fa-chevron-down"></i>
              </span>
              <div class="dropdown-content">
                <router-link to="/recommendation">Song Recommendation</router-link>
                <router-link to="/recommendationWithPlayList">Playlist-based Recommendation</router-link>
              </div>
            </div>
          </div>
        </nav>
      </div>
    </header>

    <div class="auth-container">
      <div class="auth-status">
        <span v-if="isAuthorized" class="auth-indicator authorized">
          <i class="fas fa-check-circle"></i> Authorized
        </span>
        <span v-else class="auth-indicator unauthorized">
          <i class="fas fa-times-circle"></i> Not Authorized
        </span>
        <button @click="authorize" class="auth-button">
          <i class="fab fa-spotify"></i> Connect with Spotify
        </button>
      </div>
    </div>

    <main>
      <router-view :baseURL="baseURL"></router-view>
    </main>

    <footer>
      <p>© 2023 Spotify Playlist Manager</p>
    </footer>
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
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap');
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css');

:root {
  --spotify-green: #1DB954;
  --spotify-black: #191414;
  --spotify-dark-gray: #282828;
  --spotify-light-gray: #B3B3B3;
  --spotify-white: #FFFFFF;
  --transition-speed: 0.3s;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Montserrat', sans-serif;
  background-color: var(--spotify-black);
  color: var(--spotify-white);
  line-height: 1.6;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Header */
header {
  background-color: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  font-size: 1.4rem;
  font-weight: 700;
}

.logo a {
  text-decoration: none;
  color: var(--spotify-white);
}

.spotify-text {
  color: var(--spotify-green);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.nav-item {
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: var(--spotify-white);
  cursor: pointer;
  padding: 0.5rem;
  position: relative;
  text-decoration: none;
}

.nav-item:hover {
  color: var(--spotify-green);
}

/* Dropdown menus */
.dropdown {
  position: relative;
}

.dropdown-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: var(--spotify-dark-gray);
  min-width: 200px;
  border-radius: 4px;
  padding: 0.5rem 0;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 0.5rem;
}

.dropdown-content:before {
  content: '';
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 8px solid var(--spotify-dark-gray);
}

.dropdown:hover .dropdown-content {
  display: block;
  animation: fadeIn 0.2s ease-out;
}

.dropdown-content a {
  color: var(--spotify-white);
  padding: 0.75rem 1rem;
  text-decoration: none;
  display: block;
  font-size: 0.9rem;
  transition: background-color var(--transition-speed);
}

.dropdown-content a:hover {
  background-color: rgba(29, 185, 84, 0.1);
  color: var(--spotify-green);
}

/* Auth container */
.auth-container {
  background-color: var(--spotify-dark-gray);
  padding: 0.75rem 2rem;
}

.auth-status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 1rem;
  max-width: 1400px;
  margin: 0 auto;
}

.auth-indicator {
  font-size: 0.85rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.authorized {
  color: var(--spotify-green);
}

.unauthorized {
  color: #ff5151;
}

.auth-button {
  background-color: var(--spotify-green);
  color: var(--spotify-white);
  border: none;
  border-radius: 50px;
  padding: 0.5rem 1.2rem;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all var(--transition-speed);
}

.auth-button:hover {
  background-color: #1ed760;
  transform: scale(1.05);
}

/* Main content */
main {
  flex: 1;
  padding: 2rem;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
}

/* Footer */
footer {
  background-color: var(--spotify-dark-gray);
  padding: 1rem;
  text-align: center;
  font-size: 0.8rem;
  color: var(--spotify-light-gray);
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px) translateX(-50%); }
  to { opacity: 1; transform: translateY(0) translateX(-50%); }
}

/* Router link active state */
.router-link-active {
  color: var(--spotify-green) !important;
  font-weight: 700;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 1rem;
  }
  
  .nav-links {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>