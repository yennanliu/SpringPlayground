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
            
            <div class="nav-item-group">
              <router-link to="/profile" class="nav-item">
                <i class="fas fa-user"></i> Profile
              </router-link>
              <div class="nav-submenu">
                <router-link to="/profile" class="submenu-item">
                  <i class="fas fa-list"></i> Your Playlists
                </router-link>
              </div>
            </div>
            
            <div class="nav-item-group">
              <router-link to="/playlist" class="nav-item">
                <i class="fas fa-plus-circle"></i> Create
              </router-link>
              <div class="nav-submenu">
                <router-link to="/playlist" class="submenu-item">
                  <i class="fas fa-music"></i> Create Playlist
                </router-link>
              </div>
            </div>

            <div class="nav-item-group">
              <span class="nav-item">
                <i class="fas fa-search"></i> Search
              </span>
              <div class="nav-submenu">
                <router-link to="/search_album" class="submenu-item">
                  <i class="fas fa-compact-disc"></i> Search Album
                </router-link>
                <router-link to="/search_artist" class="submenu-item">
                  <i class="fas fa-user-circle"></i> Search Artist
                </router-link>
                <router-link to="/album" class="submenu-item">
                  <i class="fas fa-fingerprint"></i> Search by Album ID
                </router-link>
              </div>
            </div>

            <div class="nav-item-group">
              <span class="nav-item">
                <i class="fas fa-music"></i> Recommendation
              </span>
              <div class="nav-submenu">
                <router-link to="/recommendation" class="submenu-item">
                  <i class="fas fa-magic"></i> Song Recommendation
                </router-link>
                <router-link to="/recommendationWithPlayList" class="submenu-item">
                  <i class="fas fa-list-alt"></i> Playlist-based Recommendation
                </router-link>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="mobile-menu-toggle" @click="toggleMobileMenu">
          <i class="fas fa-bars"></i>
        </div>
      </div>
    </header>

    <!-- Mobile menu overlay -->
    <div class="mobile-menu" :class="{ 'active': mobileMenuOpen }">
      <div class="mobile-menu-header">
        <div class="logo">
          <span class="spotify-text">Spotify</span> Playlist Manager
        </div>
        <button class="close-menu" @click="toggleMobileMenu">
          <i class="fas fa-times"></i>
        </button>
      </div>
      
      <div class="mobile-nav-links">
        <router-link to="/" class="mobile-nav-item" @click="toggleMobileMenu">
          <i class="fas fa-home"></i> Home
        </router-link>
        
        <div class="mobile-accordion">
          <div class="mobile-accordion-header" @click="toggleAccordion('profile')">
            <i class="fas fa-user"></i> Profile
            <i class="fas" :class="accordionOpen.profile ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="mobile-accordion-content" :class="{ 'open': accordionOpen.profile }">
            <router-link to="/profile" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-list"></i> Your Playlists
            </router-link>
          </div>
        </div>
        
        <div class="mobile-accordion">
          <div class="mobile-accordion-header" @click="toggleAccordion('create')">
            <i class="fas fa-plus-circle"></i> Create
            <i class="fas" :class="accordionOpen.create ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="mobile-accordion-content" :class="{ 'open': accordionOpen.create }">
            <router-link to="/playlist" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-music"></i> Create Playlist
            </router-link>
          </div>
        </div>
        
        <div class="mobile-accordion">
          <div class="mobile-accordion-header" @click="toggleAccordion('search')">
            <i class="fas fa-search"></i> Search
            <i class="fas" :class="accordionOpen.search ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="mobile-accordion-content" :class="{ 'open': accordionOpen.search }">
            <router-link to="/search_album" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-compact-disc"></i> Search Album
            </router-link>
            <router-link to="/search_artist" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-user-circle"></i> Search Artist
            </router-link>
            <router-link to="/album" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-fingerprint"></i> Search by Album ID
            </router-link>
          </div>
        </div>
        
        <div class="mobile-accordion">
          <div class="mobile-accordion-header" @click="toggleAccordion('recommendation')">
            <i class="fas fa-music"></i> Recommendation
            <i class="fas" :class="accordionOpen.recommendation ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="mobile-accordion-content" :class="{ 'open': accordionOpen.recommendation }">
            <router-link to="/recommendation" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-magic"></i> Song Recommendation
            </router-link>
            <router-link to="/recommendationWithPlayList" class="mobile-nav-item" @click="toggleMobileMenu">
              <i class="fas fa-list-alt"></i> Playlist-based Recommendation
            </router-link>
          </div>
        </div>
      </div>
    </div>

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
      mobileMenuOpen: false,
      accordionOpen: {
        profile: false,
        create: false,
        search: false,
        recommendation: false
      }
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
    toggleMobileMenu() {
      this.mobileMenuOpen = !this.mobileMenuOpen;
      // Disable body scroll when menu is open
      document.body.style.overflow = this.mobileMenuOpen ? 'hidden' : '';
    },
    toggleAccordion(section) {
      this.accordionOpen[section] = !this.accordionOpen[section];
    }
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
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.nav-item:hover {
  color: var(--spotify-green);
}

/* New Navigation Style */
.nav-item-group {
  position: relative;
  padding: 0.5rem 0;
}

.nav-submenu {
  position: absolute;
  top: 100%;
  left: 0;
  min-width: 220px;
  background-color: var(--spotify-dark-gray);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  transition: all 0.3s ease;
  z-index: 100;
}

.nav-item-group:hover .nav-submenu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.85rem 1.25rem;
  color: var(--spotify-white);
  text-decoration: none;
  transition: background-color 0.2s ease, color 0.2s ease;
  font-size: 0.85rem;
  border-left: 3px solid transparent;
}

.submenu-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
  color: var(--spotify-green);
  border-left-color: var(--spotify-green);
}

.mobile-menu-toggle {
  display: none;
  font-size: 1.5rem;
  color: var(--spotify-white);
  cursor: pointer;
}

/* Mobile Menu */
.mobile-menu {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: var(--spotify-black);
  z-index: 2000;
  transform: translateX(-100%);
  transition: transform 0.4s ease;
  overflow-y: auto;
}

.mobile-menu.active {
  transform: translateX(0);
}

.mobile-menu-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.close-menu {
  background: none;
  border: none;
  color: var(--spotify-white);
  font-size: 1.5rem;
  cursor: pointer;
}

.mobile-nav-links {
  padding: 1.5rem;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  color: var(--spotify-white);
  text-decoration: none;
  font-size: 1.1rem;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.mobile-nav-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
}

.mobile-accordion {
  margin-bottom: 0.5rem;
  border-radius: 8px;
  overflow: hidden;
}

.mobile-accordion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: rgba(255, 255, 255, 0.03);
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 600;
}

.mobile-accordion-content {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;
  background-color: rgba(0, 0, 0, 0.2);
}

.mobile-accordion-content.open {
  max-height: 300px;
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

/* Router link active state */
.router-link-active {
  color: var(--spotify-green) !important;
  font-weight: 700;
}

/* Responsive adjustments */
@media (max-width: 1024px) {
  .nav-links {
    gap: 1rem;
  }
  
  .nav-item {
    font-size: 0.8rem;
    padding: 0.4rem;
  }
}

@media (max-width: 768px) {
  .nav-links {
    display: none;
  }
  
  .mobile-menu-toggle {
    display: block;
  }
  
  .navbar {
    padding: 1rem;
  }
  
  .auth-status {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
    padding: 1rem 0;
  }
  
  .auth-button {
    width: 100%;
    justify-content: center;
  }
  
  main {
    padding: 1.5rem 1rem;
  }
}

/* Fix for iOS Safari with missing backdrop-filter support */
@supports not (backdrop-filter: blur(10px)) {
  header {
    background-color: rgba(0, 0, 0, 0.95);
  }
}
</style>