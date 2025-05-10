<template>
  <div id="home">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="container">
        <div class="hero-content">
          <h1>Data Platform Service</h1>
          <p class="hero-description">
            Streamline your data processing with our powerful platform
          </p>
          <div class="hero-cta">
            <router-link :to="{ name: 'ListCluster' }" class="btn-primary">Get Started</router-link>
            <router-link :to="{ name: 'AddJar' }" class="btn-secondary">Upload a Jar</router-link>
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
            <div class="feature-icon">
              <img src="@/assets/icon3.png" alt="Flink" width="48">
            </div>
            <h3>Apache Flink</h3>
            <p>Powerful stream processing for real-time analytics</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <img src="@/assets/icon3.png" alt="Zeppelin" width="48">
            </div>
            <h3>Apache Zeppelin</h3>
            <p>Web-based notebook for interactive data analytics</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <img src="@/assets/icon3.png" alt="SQL" width="48">
            </div>
            <h3>SQL Jobs</h3>
            <p>Run SQL queries on your data without complex code</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <img src="@/assets/icon3.png" alt="Cluster" width="48">
            </div>
            <h3>Cluster Management</h3>
            <p>Easy deployment and management of computing resources</p>
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
            <h3>Connect Cluster</h3>
            <p>Set up your computing cluster for data processing</p>
          </div>
          <div class="step">
            <div class="step-number">2</div>
            <h3>Upload Jobs</h3>
            <p>Submit your Flink JAR or SQL jobs to the platform</p>
          </div>
          <div class="step">
            <div class="step-number">3</div>
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
            <h3>Flink UI</h3>
            <p>Access the Apache Flink dashboard</p>
          </a>
          <a href="http://localhost:9080/#/" target="_blank" class="link-card">
            <h3>Zeppelin UI</h3>
            <p>Work with interactive notebooks</p>
          </a>
          <a href="http://localhost:9999/swagger-ui.html" target="_blank" class="link-card">
            <h3>Swagger UI</h3>
            <p>Explore and test the REST API</p>
          </a>
          <a href="https://github.com/yennanliu/SpringPlayground/tree/main/SpringDataPlatform" target="_blank" class="link-card">
            <h3>GitHub</h3>
            <p>View the project source code</p>
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
  padding: 120px 0;
  position: relative;
}

.hero-section::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url(../assets/home5.jpeg) center center/cover no-repeat;
  opacity: 0.4;
}

.hero-content {
  position: relative;
  max-width: 650px;
  z-index: 1;
}

.hero-content h1 {
  font-size: 52px;
  font-weight: 700;
  margin-bottom: 20px;
  line-height: 1.2;
}

.hero-description {
  font-size: 22px;
  margin-bottom: 40px;
  line-height: 1.5;
}

.hero-cta {
  display: flex;
  gap: 16px;
}

.btn-primary {
  background-color: #ffffff;
  color: #000000;
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  text-decoration: none;
  display: inline-block;
  transition: all 0.2s ease;
}

.btn-primary:hover {
  background-color: #eeeeee;
}

.btn-secondary {
  background-color: transparent;
  color: #ffffff;
  padding: 16px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  border: 1px solid #ffffff;
  text-decoration: none;
  display: inline-block;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* Features Section */
.features-section {
  padding: 80px 0;
  background-color: #ffffff;
}

.section-title {
  font-size: 36px;
  margin-bottom: 50px;
  text-align: center;
  font-weight: 700;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  padding: 30px;
  border-radius: 8px;
  background-color: #f8f8f8;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 15px;
  font-weight: 600;
}

.feature-card p {
  color: #555555;
  line-height: 1.6;
}

/* How It Works Section */
.how-it-works-section {
  padding: 80px 0;
  background-color: #f8f8f8;
}

.steps-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 30px;
}

.step {
  text-align: center;
  padding: 20px;
}

.step-number {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #000000;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 20px;
  font-weight: 600;
}

.step h3 {
  font-size: 20px;
  margin-bottom: 15px;
}

.step p {
  color: #555555;
}

/* Links Section */
.links-section {
  padding: 80px 0;
  background-color: #ffffff;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.link-card {
  background-color: #f8f8f8;
  padding: 30px;
  border-radius: 8px;
  transition: all 0.3s ease;
  text-decoration: none;
  color: #000000;
}

.link-card:hover {
  background-color: #000000;
  color: #ffffff;
  transform: translateY(-5px);
}

.link-card h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.link-card p {
  color: #555555;
  transition: all 0.3s ease;
}

.link-card:hover p {
  color: #cccccc;
}

/* Responsive Styles */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 42px;
  }
  
  .hero-description {
    font-size: 18px;
  }
  
  .hero-cta {
    flex-direction: column;
  }
  
  .section-title {
    font-size: 28px;
  }
  
  .hero-section {
    padding: 80px 0;
  }
  
  .features-section,
  .how-it-works-section,
  .links-section {
    padding: 60px 0;
  }
}
</style>