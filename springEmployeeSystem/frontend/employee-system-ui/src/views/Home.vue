<template>
  <div id="home">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="overlay"></div>
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">Welcome to<br>Employee Portal</h1>
          <p class="hero-description">
            Simplify managing your team, tracking vacations, and collaborating across departments with our modern workplace platform
          </p>
          <div class="hero-cta">
            <a href="#main-content" class="btn btn-primary btn-lg">Get Started</a>
            <a href="#" class="btn btn-outline-light ml-3">Learn More</a>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div id="main-content" class="main-content">
      <div class="container">
        <!-- Features Section -->
        <section class="features-section">
          <div class="section-title-wrapper">
            <h2 class="text-center mb-2">Everything You Need</h2>
            <p class="text-center section-subtitle">Tools to make your team more productive</p>
          </div>
          
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-people-fill"></i>
              </div>
              <h3>Team Management</h3>
              <p>Streamline onboarding, manage roles, and maintain up-to-date employee information.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-calendar-check"></i>
              </div>
              <h3>Vacation Tracking</h3>
              <p>Easily manage time off requests and keep track of your team's availability.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-ticket-perforated"></i>
              </div>
              <h3>Ticketing System</h3>
              <p>Resolve issues efficiently with our integrated ticketing workflow.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-bar-chart"></i>
              </div>
              <h3>Reporting</h3>
              <p>Get insights into team performance and productivity with custom reports.</p>
            </div>
          </div>
        </section>

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
              <img src="https://images.unsplash.com/photo-1497366754035-f200968a6e72?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80" alt="Office" class="card-image">
              <div class="news-content">
                <span class="news-badge">Company News</span>
                <h3>Company Expands to New Markets</h3>
                <p>We're excited to announce our expansion into three new international markets.</p>
                <p class="card-date">Nov 10, 2023</p>
              </div>
            </div>
            
            <div class="news-card">
              <img src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80" alt="Team" class="card-image">
              <div class="news-content">
                <span class="news-badge">Benefits</span>
                <h3>New Benefits Package</h3>
                <p>Enhanced health and wellness benefits will be available starting next quarter.</p>
                <p class="card-date">Nov 5, 2023</p>
              </div>
            </div>
            
            <div class="news-card">
              <img src="https://images.unsplash.com/photo-1475721027785-f74eccf877e2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80" alt="Conference" class="card-image">
              <div class="news-content">
                <span class="news-badge">Events</span>
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
  height: 85vh;
  min-height: 600px;
  position: relative;
  background: url('https://images.unsplash.com/photo-1604328698692-f76ea9498e76?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.4) 100%);
}

.hero-content {
  max-width: 700px;
  color: white;
  padding: 0 16px;
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  margin-bottom: 24px;
  line-height: 1.2;
  letter-spacing: -0.02em;
  color: var(--airbnb-primary);
}

.hero-description {
  font-size: 20px;
  margin-bottom: 40px;
  line-height: 1.6;
  font-weight: 400;
  opacity: 0.9;
}

.hero-cta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.btn-lg {
  padding: 14px 32px;
  font-size: 18px;
  font-weight: 600;
}

.btn-outline-light {
  color: white;
  border: 2px solid white;
  background-color: transparent;
  border-radius: var(--border-radius);
  padding: 14px 32px;
  font-size: 18px;
  font-weight: 600;
  transition: var(--transition);
}

.btn-outline-light:hover {
  background-color: white;
  color: var(--airbnb-dark);
  transform: translateY(-2px);
}

/* Features Section */
.features-section {
  padding: 80px 0;
  background-color: var(--airbnb-white);
}

.section-title-wrapper {
  margin-bottom: 64px;
}

.section-subtitle {
  font-size: 20px;
  color: var(--airbnb-light);
  max-width: 600px;
  margin: 0 auto;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
  margin-bottom: 32px;
}

.feature-card {
  background-color: var(--airbnb-white);
  padding: 32px;
  border-radius: var(--border-radius);
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
  transition: var(--transition);
  text-align: center;
}

.feature-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.feature-icon {
  font-size: 48px;
  color: var(--airbnb-primary);
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 22px;
  margin-bottom: 16px;
  font-weight: 700;
}

.feature-card p {
  color: var(--airbnb-light);
  font-size: 16px;
  line-height: 1.6;
}

/* Main Content */
.main-content {
  padding: 64px 0;
  background-color: var(--airbnb-bg);
}

.content-section {
  margin-bottom: 80px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.section-header h2 {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
}

.view-all-link {
  color: var(--airbnb-primary);
  font-weight: 600;
  font-size: 18px;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.view-all-link i {
  margin-left: 8px;
  transition: transform 0.2s ease;
}

.view-all-link:hover i {
  transform: translateX(4px);
}

/* Cards */
.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 32px;
}

.announcement-card, .news-card {
  background: white;
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
  transition: var(--transition);
}

.announcement-card:hover, .news-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.announcement-card {
  display: flex;
  padding: 28px;
}

.card-icon {
  font-size: 32px;
  color: var(--airbnb-primary);
  margin-right: 24px;
}

.card-content h3, .news-content h3 {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 12px;
}

.card-content p, .news-content p {
  font-size: 16px;
  color: var(--airbnb-light);
  margin-bottom: 12px;
  line-height: 1.6;
}

.card-date {
  font-size: 14px;
  color: var(--airbnb-primary);
  font-weight: 600;
  margin-top: 12px;
}

.news-card {
  display: flex;
  flex-direction: column;
}

.card-image {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.news-content {
  padding: 24px;
  position: relative;
}

.news-badge {
  position: absolute;
  top: -14px;
  left: 24px;
  background-color: var(--airbnb-primary);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
}

@media (max-width: 992px) {
  .hero-title {
    font-size: 48px;
  }
  
  .hero-description {
    font-size: 18px;
  }
  
  .cards-container {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 40px;
  }
  
  .hero-description {
    font-size: 17px;
  }
  
  .cards-container {
    grid-template-columns: 1fr;
  }
  
  .announcement-card {
    flex-direction: column;
  }
  
  .card-icon {
    margin-right: 0;
    margin-bottom: 16px;
  }
  
  .feature-card {
    padding: 24px;
  }
}
</style>