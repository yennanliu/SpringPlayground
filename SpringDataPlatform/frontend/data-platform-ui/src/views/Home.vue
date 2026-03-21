<template>
  <div id="home">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-overlay"></div>
      <div class="container">
        <div class="hero-content">
          <h1>Data Platform Service</h1>
          <p class="hero-description">
            Streamline your data processing with our powerful platform
          </p>
          <div class="hero-cta">
            <router-link :to="{ name: 'ListCluster' }" class="btn-hero-primary">
              Get Started
              <svg class="btn-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M5 12h14M12 5l7 7-7 7"/>
              </svg>
            </router-link>
            <router-link :to="{ name: 'AddJar' }" class="btn-hero-secondary">Upload a Jar</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
      <div class="container">
        <h2 class="section-title">Platform Features</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon feature-icon-flink">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
              </svg>
            </div>
            <h3>Apache Flink</h3>
            <p>Powerful stream processing for real-time analytics and event-driven applications</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon feature-icon-zeppelin">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
                <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
              </svg>
            </div>
            <h3>Apache Zeppelin</h3>
            <p>Web-based notebook for interactive data analytics and visualization</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon feature-icon-sql">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <ellipse cx="12" cy="5" rx="9" ry="3"/>
                <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
                <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
              </svg>
            </div>
            <h3>SQL Jobs</h3>
            <p>Run SQL queries on your data without complex code or setup</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon feature-icon-cluster">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="2" y="2" width="20" height="8" rx="2" ry="2"/>
                <rect x="2" y="14" width="20" height="8" rx="2" ry="2"/>
                <line x1="6" y1="6" x2="6.01" y2="6"/>
                <line x1="6" y1="18" x2="6.01" y2="18"/>
              </svg>
            </div>
            <h3>Cluster Management</h3>
            <p>Easy deployment and management of computing resources at scale</p>
          </div>
        </div>
      </div>
    </section>

    <!-- How It Works Section -->
    <section class="how-it-works-section">
      <div class="container">
        <h2 class="section-title">How It Works</h2>
        <div class="steps-container">
          <div class="step">
            <div class="step-number">1</div>
            <div class="step-connector"></div>
            <h3>Connect Cluster</h3>
            <p>Set up your computing cluster for data processing</p>
          </div>
          <div class="step">
            <div class="step-number">2</div>
            <div class="step-connector"></div>
            <h3>Upload Jobs</h3>
            <p>Submit your Flink JAR or SQL jobs to the platform</p>
          </div>
          <div class="step">
            <div class="step-number">3</div>
            <div class="step-connector"></div>
            <h3>Process Data</h3>
            <p>Run your data processing jobs with powerful analytics</p>
          </div>
          <div class="step">
            <div class="step-number">4</div>
            <h3>View Results</h3>
            <p>Analyze the results with interactive dashboards</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Links Section -->
    <section class="links-section">
      <div class="container">
        <h2 class="section-title">Quick Links</h2>
        <div class="links-grid">
          <a href="http://localhost:8081/#/overview" target="_blank" class="link-card">
            <div class="link-card-content">
              <h3>Flink UI</h3>
              <p>Access the Apache Flink dashboard</p>
            </div>
            <div class="link-arrow">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M7 17L17 7M17 7H7M17 7v10"/>
              </svg>
            </div>
          </a>
          <a href="http://localhost:9080/#/" target="_blank" class="link-card">
            <div class="link-card-content">
              <h3>Zeppelin UI</h3>
              <p>Work with interactive notebooks</p>
            </div>
            <div class="link-arrow">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M7 17L17 7M17 7H7M17 7v10"/>
              </svg>
            </div>
          </a>
          <a href="http://localhost:9999/swagger-ui.html" target="_blank" class="link-card">
            <div class="link-card-content">
              <h3>Swagger UI</h3>
              <p>Explore and test the REST API</p>
            </div>
            <div class="link-arrow">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M7 17L17 7M17 7H7M17 7v10"/>
              </svg>
            </div>
          </a>
          <a href="https://github.com/yennanliu/SpringPlayground/tree/main/SpringDataPlatform" target="_blank" class="link-card">
            <div class="link-card-content">
              <h3>GitHub</h3>
              <p>View the project source code</p>
            </div>
            <div class="link-arrow">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M7 17L17 7M17 7H7M17 7v10"/>
              </svg>
            </div>
          </a>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "Home",
  props: ["baseURL"],
  data() {
    return {};
  },
  mounted() {},
};
</script>

<style>
#home {
  overflow-x: hidden;
}

/* Hero Section */
.hero-section {
  background-color: #000000;
  color: #ffffff;
  padding: 140px 0;
  position: relative;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url(../assets/home5.jpeg) center center/cover no-repeat;
  opacity: 0.35;
}

.hero-overlay::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.4) 100%);
}

.hero-content {
  position: relative;
  max-width: 650px;
  z-index: 1;
}

.hero-content h1 {
  font-size: 56px;
  font-weight: 700;
  margin-bottom: 24px;
  line-height: 1.1;
  letter-spacing: -0.02em;
}

.hero-description {
  font-size: 22px;
  margin-bottom: 40px;
  line-height: 1.6;
  opacity: 0.9;
}

.hero-cta {
  display: flex;
  gap: 16px;
}

.btn-hero-primary {
  background-color: #ffffff;
  color: #000000;
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.btn-hero-primary:hover {
  background-color: #f0c14b;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(240, 193, 75, 0.3);
}

.btn-hero-primary .btn-icon {
  transition: transform 0.25s ease;
}

.btn-hero-primary:hover .btn-icon {
  transform: translateX(4px);
}

.btn-hero-secondary {
  background-color: transparent;
  color: #ffffff;
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  text-decoration: none;
  display: inline-block;
  transition: all 0.25s ease;
}

.btn-hero-secondary:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.5);
}

/* Features Section */
.features-section {
  padding: 100px 0;
  background-color: #ffffff;
}

.section-title {
  font-size: 40px;
  margin-bottom: 60px;
  text-align: center;
  font-weight: 700;
  color: #000000;
  letter-spacing: -0.02em;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 30px;
}

.feature-card {
  padding: 36px 30px;
  border-radius: 16px;
  background-color: #ffffff;
  border: 1px solid #f0f0f0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08);
  border-color: transparent;
}

.feature-icon {
  width: 60px;
  height: 60px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

.feature-icon-flink {
  background-color: rgba(240, 193, 75, 0.1);
  color: #d4a83a;
}

.feature-icon-zeppelin {
  background-color: rgba(23, 162, 184, 0.1);
  color: #17a2b8;
}

.feature-icon-sql {
  background-color: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.feature-icon-cluster {
  background-color: rgba(0, 0, 0, 0.05);
  color: #495057;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1);
}

.feature-card h3 {
  font-size: 22px;
  margin-bottom: 12px;
  font-weight: 600;
  color: #000000;
}

.feature-card p {
  color: #6c757d;
  line-height: 1.7;
  margin: 0;
  font-size: 15px;
}

/* How It Works Section */
.how-it-works-section {
  padding: 100px 0;
  background-color: #fafafa;
}

.steps-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  position: relative;
}

.step {
  text-align: center;
  padding: 30px 20px;
  position: relative;
}

.step-number {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: #000000;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  font-size: 22px;
  font-weight: 700;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.step:hover .step-number {
  background-color: #f0c14b;
  color: #000000;
  transform: scale(1.1);
}

.step-connector {
  position: absolute;
  top: 58px;
  left: calc(50% + 35px);
  width: calc(100% - 56px);
  height: 2px;
  background: linear-gradient(90deg, #dee2e6, #dee2e6);
}

.step:last-child .step-connector {
  display: none;
}

.step h3 {
  font-size: 18px;
  margin-bottom: 12px;
  font-weight: 600;
  color: #000000;
}

.step p {
  color: #6c757d;
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

/* Links Section */
.links-section {
  padding: 100px 0;
  background-color: #ffffff;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
}

.link-card {
  background-color: #fafafa;
  padding: 28px;
  border-radius: 14px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  color: #000000;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  border: 1px solid transparent;
}

.link-card:hover {
  background-color: #000000;
  color: #ffffff;
  transform: translateY(-4px);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.12);
}

.link-card-content {
  flex: 1;
}

.link-card h3 {
  font-size: 18px;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.link-card p {
  color: #6c757d;
  transition: color 0.3s ease;
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
}

.link-card:hover p {
  color: rgba(255, 255, 255, 0.7);
}

.link-arrow {
  opacity: 0;
  transform: translate(-8px, 8px);
  transition: all 0.3s ease;
  color: #f0c14b;
}

.link-card:hover .link-arrow {
  opacity: 1;
  transform: translate(0, 0);
}

/* Responsive Styles */
@media (max-width: 992px) {
  .steps-container {
    grid-template-columns: repeat(2, 1fr);
  }

  .step-connector {
    display: none;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 100px 0;
  }

  .hero-content h1 {
    font-size: 40px;
  }

  .hero-description {
    font-size: 18px;
  }

  .hero-cta {
    flex-direction: column;
  }

  .section-title {
    font-size: 32px;
    margin-bottom: 40px;
  }

  .features-section,
  .how-it-works-section,
  .links-section {
    padding: 70px 0;
  }

  .steps-container {
    grid-template-columns: 1fr;
  }
}
</style>
