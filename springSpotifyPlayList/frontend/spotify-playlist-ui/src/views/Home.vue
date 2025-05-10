<template>
  <div id="home">
    <section class="hero-section">
      <div class="hero-content">
        <h1>Welcome to Spotify Playlist Manager</h1>
        <p>Discover, create and manage your Spotify playlists with ease</p>
        <div class="cta-buttons">
          <router-link to="/profile" class="cta-button primary">
            <i class="fas fa-headphones-alt"></i> Your Playlists
          </router-link>
          <router-link to="/search_album" class="cta-button secondary">
            <i class="fas fa-search"></i> Explore Music
          </router-link>
        </div>
      </div>
      <div class="hero-image">
        <img src="https://i.imgur.com/1E0OJtU.png" alt="Music Illustration" />
      </div>
    </section>

    <section class="features-section">
      <h2>Powerful Features</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-list"></i>
          </div>
          <h3>Browse Playlists</h3>
          <p>Access and manage your Spotify playlists with ease</p>
          <router-link to="/profile" class="feature-link">
            Your Playlists <i class="fas fa-arrow-right"></i>
          </router-link>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-search"></i>
          </div>
          <h3>Search Music</h3>
          <p>Find albums, artists, and explore tracks with previews</p>
          <div class="feature-links">
            <router-link to="/search_album" class="feature-link">
              Albums <i class="fas fa-arrow-right"></i>
            </router-link>
            <router-link to="/search_artist" class="feature-link">
              Artists <i class="fas fa-arrow-right"></i>
            </router-link>
          </div>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-magic"></i>
          </div>
          <h3>Smart Recommendations</h3>
          <p>Discover new music with our machine learning algorithms</p>
          <div class="feature-links">
            <router-link to="/recommendation" class="feature-link">
              Song Recommendations <i class="fas fa-arrow-right"></i>
            </router-link>
            <router-link to="/recommendationWithPlayList" class="feature-link">
              Playlist Recommendations <i class="fas fa-arrow-right"></i>
            </router-link>
          </div>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-plus-circle"></i>
          </div>
          <h3>Create & Customize</h3>
          <p>Build your own playlists to match your unique taste</p>
          <router-link to="/playlist" class="feature-link">
            Create Playlist <i class="fas fa-arrow-right"></i>
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
/* Hero Section */
.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 500px;
  padding: 3rem 2rem;
  background: linear-gradient(135deg, #121212 0%, #282828 100%);
  border-radius: 20px;
  margin-bottom: 3rem;
  overflow: hidden;
  position: relative;
}

.hero-content {
  flex: 1;
  max-width: 550px;
  z-index: 2;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
  background: linear-gradient(90deg, var(--spotify-green), #88fc9b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 2px 15px rgba(29, 185, 84, 0.2);
}

.hero-content p {
  font-size: 1.3rem;
  color: var(--spotify-light-gray);
  margin-bottom: 2.5rem;
  line-height: 1.6;
}

.hero-image {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-image img {
  max-width: 100%;
  height: auto;
  max-height: 400px;
  animation: float 6s ease-in-out infinite;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.cta-button {
  text-decoration: none;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.cta-button.primary {
  background-color: var(--spotify-green);
  color: var(--spotify-black);
}

.cta-button.secondary {
  background-color: transparent;
  color: var(--spotify-white);
  border: 2px solid var(--spotify-green);
}

.cta-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

/* Features Section */
.features-section {
  padding: 2rem 0;
}

.features-section h2 {
  text-align: center;
  font-size: 2.3rem;
  margin-bottom: 3rem;
  position: relative;
  display: inline-block;
  left: 50%;
  transform: translateX(-50%);
}

.features-section h2:after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 4px;
  background: var(--spotify-green);
  border-radius: 2px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 16px;
  padding: 2rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.feature-card:after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: var(--spotify-green);
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  width: 70px;
  height: 70px;
  background: rgba(29, 185, 84, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.feature-icon i {
  font-size: 2rem;
  color: var(--spotify-green);
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: var(--spotify-white);
}

.feature-card p {
  font-size: 1rem;
  color: var(--spotify-light-gray);
  margin-bottom: 1.5rem;
  flex-grow: 1;
}

.feature-links {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.feature-link {
  color: var(--spotify-green);
  text-decoration: none;
  font-weight: 600;
  font-size: 0.95rem;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
}

.feature-link:hover {
  transform: translateX(5px);
}

.feature-link i {
  font-size: 0.8rem;
  transition: transform 0.2s ease;
}

.feature-link:hover i {
  transform: translateX(3px);
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
  100% {
    transform: translateY(0px);
  }
}

/* Responsive Styles */
@media (max-width: 1024px) {
  .hero-section {
    flex-direction: column;
    text-align: center;
  }
  
  .hero-content {
    max-width: 100%;
    margin-bottom: 2rem;
  }
  
  .cta-buttons {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2.5rem;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .feature-card {
    max-width: 400px;
    margin: 0 auto;
  }
}
</style>