<template>
  <div id="home">
    <!-- Hero Section -->
    <div id="background-div" class="hero-section">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">Welcome to Employee Portal</h1>
          <p class="hero-description">
            Simplify managing your team, tracking vacations, and collaborating across departments
          </p>
          <a href="#main-content" class="btn btn-primary">Get Started</a>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div id="main-content" class="main-content">
      <div class="container">
        <!-- Announcements Section -->
        <section class="content-section">
          <div class="section-header">
            <h2>Announcements</h2>
            <a href="#" class="view-all-link">View all <i class="bi bi-arrow-right"></i></a>
          </div>
          
          <div class="cards-container">
            <div class="announcement-card">
              <div class="card-icon"><i class="bi bi-calendar-event"></i></div>
              <div class="card-content">
                <h3>Hackathon 2023</h3>
                <p>Join our annual hackathon to showcase your skills and win exciting prizes!</p>
                <p class="card-date">Dec 15-17, 2023</p>
              </div>
            </div>
            
            <div class="announcement-card">
              <div class="card-icon"><i class="bi bi-building"></i></div>
              <div class="card-content">
                <h3>2024 RTO Plan</h3>
                <p>New return-to-office guidelines will be implemented starting January 2024.</p>
                <p class="card-date">Starting Jan 15, 2024</p>
              </div>
            </div>
            
            <div class="announcement-card">
              <div class="card-icon"><i class="bi bi-award"></i></div>
              <div class="card-content">
                <h3>Q4 Performance Reviews</h3>
                <p>Complete your self-assessment by the end of this month.</p>
                <p class="card-date">Due Nov 30, 2023</p>
              </div>
            </div>
          </div>
        </section>

        <!-- News Section -->
        <section class="content-section">
          <div class="section-header">
            <h2>Latest News</h2>
            <a href="#" class="view-all-link">View all <i class="bi bi-arrow-right"></i></a>
          </div>
          
          <div class="cards-container">
            <div class="news-card">
              <img src="https://source.unsplash.com/random/300x200/?office" alt="Office" class="card-image">
              <div class="news-content">
                <h3>Company Expands to New Markets</h3>
                <p>We're excited to announce our expansion into three new international markets.</p>
                <p class="card-date">Nov 10, 2023</p>
              </div>
            </div>
            
            <div class="news-card">
              <img src="https://source.unsplash.com/random/300x200/?team" alt="Team" class="card-image">
              <div class="news-content">
                <h3>New Benefits Package</h3>
                <p>Enhanced health and wellness benefits will be available starting next quarter.</p>
                <p class="card-date">Nov 5, 2023</p>
              </div>
            </div>
            
            <div class="news-card">
              <img src="https://source.unsplash.com/random/300x200/?conference" alt="Conference" class="card-image">
              <div class="news-content">
                <h3>Annual Conference Registration</h3>
                <p>Registration is now open for our annual company conference in Hawaii.</p>
                <p class="card-date">Oct 28, 2023</p>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: "Home",
  props: ["baseURL", "users", "departments"],
  data() {
    return {
      category_size: 0,
      product_size: 0,
    };
  },
  mounted() {},
};
</script>
  
<style>
/* Hero Section */
.hero-section {
  height: 70vh;
  min-height: 500px;
  background: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url(../assets/home4.jpeg);
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
}

.hero-content {
  max-width: 600px;
  color: white;
  padding: 0 16px;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 24px;
}

.hero-description {
  font-size: 18px;
  margin-bottom: 32px;
  line-height: 1.5;
}

/* Main Content */
.main-content {
  padding: 64px 0;
}

.content-section {
  margin-bottom: 64px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.view-all-link {
  color: var(--airbnb-primary);
  font-weight: 500;
  text-decoration: none;
}

.view-all-link:hover {
  text-decoration: underline;
}

/* Cards */
.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.announcement-card, .news-card {
  background: white;
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.announcement-card:hover, .news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.announcement-card {
  display: flex;
  padding: 24px;
}

.card-icon {
  font-size: 24px;
  color: var(--airbnb-primary);
  margin-right: 16px;
}

.card-content h3, .news-content h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}

.card-content p, .news-content p {
  font-size: 14px;
  color: var(--airbnb-light);
  margin-bottom: 8px;
}

.card-date {
  font-size: 12px;
  color: var(--airbnb-primary);
  margin-top: 8px;
}

.news-card {
  display: flex;
  flex-direction: column;
}

.card-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.news-content {
  padding: 16px;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }
  
  .cards-container {
    grid-template-columns: 1fr;
  }
}
</style>