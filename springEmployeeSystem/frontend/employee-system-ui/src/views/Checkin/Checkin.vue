<template>
  <div id="checkin">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="overlay"></div>
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">Employee<br>Check-In</h1>
          <p class="hero-description">
            Quick and easy check-in system for tracking employee attendance and presence
          </p>
          <div class="hero-clock">
            <div class="clock-display">
              <div class="time">{{ currentTime }}</div>
              <div class="date">{{ currentDate }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="container">
        <!-- Check-in Section -->
        <section class="checkin-section">
          <div class="section-title-wrapper">
            <h2 class="text-center mb-2">Check In to Work</h2>
            <p class="text-center section-subtitle">Enter your employee ID to record your arrival</p>
          </div>
          
          <div class="checkin-form-container">
            <div class="checkin-card">
              <div class="card-header">
                <div class="status-indicator">
                  <i class="bi bi-person-check-fill"></i>
                </div>
                <h3>Employee Check-In</h3>
                <p>Please enter your employee ID below</p>
              </div>
              
              <form @submit.prevent="addCheckin" class="checkin-form">
                <div class="form-group">
                  <label for="userId" class="form-label">Employee ID</label>
                  <div class="input-group">
                    <span class="input-prefix">
                      <i class="bi bi-person-badge"></i>
                    </span>
                    <input
                      id="userId"
                      type="text"
                      class="form-control"
                      v-model="userId"
                      required
                      @input="validateUserId"
                      placeholder="Enter your employee ID"
                      :class="{ 'is-invalid': userId && !isUserIdValid, 'is-valid': userId && isUserIdValid }"
                    />
                  </div>
                  <div v-if="userId && !isUserIdValid" class="invalid-feedback">
                    Employee ID must be a valid number
                  </div>
                </div>
                
                <button 
                  type="submit" 
                  class="btn btn-primary btn-lg w-100" 
                  :disabled="!isUserIdValid || isLoading"
                  :class="{ 'loading': isLoading }"
                >
                  <span v-if="!isLoading" class="btn-content">
                    <i class="bi bi-check-circle-fill"></i>
                    Check In Now
                  </span>
                  <span v-else class="btn-content">
                    <i class="bi bi-arrow-clockwise spin"></i>
                    Processing...
                  </span>
                </button>
              </form>
            </div>
          </div>
        </section>

        <!-- Features Section -->
        <section class="features-section">
          <div class="section-title-wrapper">
            <h2 class="text-center mb-2">Check-In Benefits</h2>
            <p class="text-center section-subtitle">Why daily check-ins matter</p>
          </div>
          
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-clock-history"></i>
              </div>
              <h3>Time Tracking</h3>
              <p>Accurately track your work hours and attendance for payroll and reporting.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-shield-check"></i>
              </div>
              <h3>Security</h3>
              <p>Secure workplace access and maintain building security compliance.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-bar-chart-line"></i>
              </div>
              <h3>Analytics</h3>
              <p>Generate insights on workplace utilization and team collaboration patterns.</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">
                <i class="bi bi-people-fill"></i>
              </div>
              <h3>Team Presence</h3>
              <p>Know who's in the office and coordinate better with your teammates.</p>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import swal from "sweetalert";
import axios from "axios";

export default {
  name: "Checkin",
  data() {
    return {
      userId: null,
      isUserIdValid: false,
      isLoading: false,
      currentTime: '',
      currentDate: '',
      clockInterval: null,
    };
  },
  mounted() {
    this.updateClock();
    this.clockInterval = setInterval(this.updateClock, 1000);
  },
  beforeUnmount() {
    if (this.clockInterval) {
      clearInterval(this.clockInterval);
    }
  },
  methods: {
    updateClock() {
      const now = new Date();
      
      // Format time (12-hour format with AM/PM)
      this.currentTime = now.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: true
      });
      
      // Format date
      this.currentDate = now.toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },
    
    validateUserId() {
      // Check if userId is a valid integer
      const trimmedValue = this.userId?.toString().trim();
      this.isUserIdValid = trimmedValue && Number.isInteger(Number(trimmedValue)) && Number(trimmedValue) > 0;
    },
    
    async addCheckin() {
      // Proceed only if userId is valid
      if (!this.isUserIdValid) {
        return;
      }

      this.isLoading = true;

      const newCheckin = {
        id: null,
        userId: this.userId,
        createTime: null,
      };

      try {
        const res = await axios.post("http://localhost:9998/checkin/add", newCheckin, {
          headers: {
            "Content-Type": "application/json",
          },
        });

        console.log(res);
        this.$emit("fetchData");
        
        // Success message with current time
        swal({
          title: "Check-in Successful!",
          text: `Employee ID ${this.userId} checked in at ${this.currentTime}`,
          icon: "success",
          closeOnClickOutside: false,
          button: "Continue",
        }).then(() => {
          this.$router.push({ name: "Home" });
        });
        
        // Reset form
        this.userId = null;
        this.isUserIdValid = false;
        
      } catch (err) {
        // Handle error when user ID does not exist or other errors
        const errorMessage = err.response?.data?.message || "Check-in failed! Please try again.";
        swal({
          title: "Check-in Failed",
          text: errorMessage,
          icon: "error",
          closeOnClickOutside: false,
          button: "Try Again",
        });
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },
  },
};
</script>

<style scoped>
/* Hero Section */
.hero-section {
  height: 75vh;
  min-height: 500px;
  position: relative;
  background: url('https://images.unsplash.com/photo-1497366216548-37526070297c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80');
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
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 24px;
  line-height: 1.2;
  letter-spacing: -0.02em;
  color: #FF385C;
}

.hero-description {
  font-size: 18px;
  margin-bottom: 32px;
  line-height: 1.6;
  font-weight: 400;
  opacity: 0.9;
}

.hero-clock {
  margin-top: 32px;
}

.clock-display {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  max-width: 300px;
}

.time {
  font-size: 32px;
  font-weight: 700;
  color: #FF385C;
  margin-bottom: 8px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.date {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
}

/* Main Content */
.main-content {
  padding: 64px 0;
  background-color: #F7F7F7;
}

.checkin-section {
  margin-bottom: 80px;
}

.section-title-wrapper {
  margin-bottom: 48px;
}

.section-title-wrapper h2 {
  font-size: 32px;
  font-weight: 700;
  color: #484848;
}

.section-subtitle {
  font-size: 18px;
  color: #717171;
  max-width: 600px;
  margin: 0 auto;
}

.checkin-form-container {
  display: flex;
  justify-content: center;
  margin-bottom: 48px;
}

.checkin-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
  padding: 40px;
  max-width: 500px;
  width: 100%;
  transition: all 0.3s ease;
}

.checkin-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.card-header {
  text-align: center;
  margin-bottom: 32px;
}

.status-indicator {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #FF385C, #FF5A5F);
  border-radius: 50%;
  margin-bottom: 24px;
  font-size: 36px;
  color: white;
}

.card-header h3 {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
  color: #484848;
}

.card-header p {
  color: #717171;
  font-size: 16px;
  margin: 0;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #484848;
  font-size: 16px;
}

.input-group {
  position: relative;
  display: flex;
  align-items: center;
}

.input-prefix {
  position: absolute;
  left: 16px;
  color: #717171;
  font-size: 18px;
  z-index: 1;
}

.form-control {
  width: 100%;
  padding: 16px 16px 16px 48px;
  border: 2px solid #E0E0E0;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.2s ease;
  background-color: #FAFAFA;
}

.form-control:focus {
  outline: none;
  border-color: #FF385C;
  background-color: white;
  box-shadow: 0 0 0 3px rgba(255, 56, 92, 0.1);
}

.form-control.is-valid {
  border-color: #28a745;
  background-color: white;
}

.form-control.is-invalid {
  border-color: #dc3545;
  background-color: white;
}

.invalid-feedback {
  display: block;
  color: #dc3545;
  font-size: 14px;
  margin-top: 8px;
}

.btn {
  border: none;
  border-radius: 12px;
  font-weight: 600;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  cursor: pointer;
}

.btn-primary {
  background: linear-gradient(135deg, #FF385C, #FF5A5F);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #E91E63, #FF385C);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 56, 92, 0.3);
}

.btn-primary:disabled {
  background: #CCCCCC;
  cursor: not-allowed;
  transform: none;
}

.btn-lg {
  padding: 16px 32px;
  font-size: 18px;
  min-height: 56px;
}

.btn-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-content i {
  font-size: 20px;
}

.loading .btn-content i {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Features Section */
.features-section {
  padding: 64px 0;
  background-color: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
  margin-top: 48px;
}

.feature-card {
  background-color: white;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
  transition: all 0.3s ease;
  text-align: center;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.feature-icon {
  font-size: 48px;
  color: #FF385C;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 16px;
  font-weight: 700;
  color: #484848;
}

.feature-card p {
  color: #717171;
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
}

/* Responsive Design */
@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }
  
  .hero-description {
    font-size: 16px;
  }
  
  .clock-display {
    padding: 20px;
  }
  
  .time {
    font-size: 28px;
  }
  
  .checkin-card {
    padding: 24px;
    margin: 0 16px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }
  
  .feature-card {
    padding: 24px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 32px;
  }
  
  .time {
    font-size: 24px;
  }
  
  .date {
    font-size: 14px;
  }
  
  .checkin-card {
    padding: 20px;
  }
  
  .status-indicator {
    width: 60px;
    height: 60px;
    font-size: 28px;
  }
}
</style>