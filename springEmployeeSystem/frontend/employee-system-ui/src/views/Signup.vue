<!--
    https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Signup.vue
-->
<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- Logo -->
      <div class="text-center mb-4">
        <router-link :to="{ name: 'Home' }">
          <img id="logo" src="../assets/icon2.png" alt="Logo" />
        </router-link>
      </div>
      
      <h1 class="auth-title">Create account</h1>
      <p class="auth-subtitle">Join our team management platform</p>
      
      <form @submit="signup" class="auth-form">
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="firstname">First Name</label>
            <div class="input-with-icon">
              <i class="bi bi-person"></i>
              <input
                type="text"
                id="firstname"
                class="form-control"
                v-model="firstName"
                placeholder="Enter first name"
                required
              />
            </div>
          </div>
          <div class="form-group col-md-6">
            <label for="lastname">Last Name</label>
            <div class="input-with-icon">
              <i class="bi bi-person"></i>
              <input
                type="text"
                id="lastname"
                class="form-control"
                v-model="lastName"
                placeholder="Enter last name"
                required
              />
            </div>
          </div>
        </div>
        
        <div class="form-group">
          <label for="email">Email</label>
          <div class="input-with-icon">
            <i class="bi bi-envelope"></i>
            <input
              type="email"
              id="email"
              class="form-control"
              v-model="email"
              placeholder="Enter your email"
              required
            />
          </div>
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <div class="input-with-icon">
            <i class="bi bi-lock"></i>
            <input
              type="password"
              id="password"
              class="form-control"
              v-model="password"
              placeholder="Create a password"
              required
            />
            <div class="password-strength">
              <span class="strength-indicator strong"></span>
              <span class="strength-text">Strong password</span>
            </div>
          </div>
        </div>
        
        <div class="form-group">
          <label for="role">Role</label>
          <div class="input-with-icon">
            <i class="bi bi-briefcase"></i>
            <select id="role" class="form-control" v-model="role" required>
              <option value="" disabled selected>Select a role</option>
              <option value="admin">Admin</option>
              <option value="manager">Manager</option>
              <option value="employee">Employee</option>
            </select>
          </div>
        </div>
        
        <div class="form-check mb-4">
          <input class="form-check-input" type="checkbox" id="agreeTerms" required>
          <label class="form-check-label" for="agreeTerms">
            I agree to the <a href="#" class="terms-link">Terms of Service</a> and <a href="#" class="terms-link">Privacy Policy</a>
          </label>
        </div>
        
        <button type="submit" class="btn btn-primary btn-block btn-lg">
          Create Account
          <div
            v-if="loading"
            class="spinner-border spinner-border-sm ml-2"
            role="status"
          >
            <span class="sr-only">Loading...</span>
          </div>
        </button>
      </form>
      
      <div class="divider">
        <span>or sign up with</span>
      </div>
      
      <div class="oauth-buttons">
        <button class="btn btn-oauth">
          <img src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg" alt="Google" class="oauth-icon">
          Google
        </button>
        <button class="btn btn-oauth">
          <i class="bi bi-microsoft"></i>
          Microsoft
        </button>
        <button class="btn btn-oauth">
          <i class="bi bi-apple"></i>
          Apple
        </button>
      </div>
      
      <div class="auth-footer">
        <p class="text-center">
          Already have an account?
          <router-link :to="{ name: 'Signin' }" class="signin-link">
            Log in
          </router-link>
        </p>
      </div>
    </div>
    
    <div class="auth-image">
      <div class="image-overlay"></div>
      <div class="image-content">
        <h2>Join Our Team</h2>
        <p>Create an account to manage your team efficiently, track time off, and collaborate with your colleagues.</p>
        <div class="features-list">
          <div class="feature-item">
            <i class="bi bi-people-fill"></i>
            <span>Team Management</span>
          </div>
          <div class="feature-item">
            <i class="bi bi-calendar-check"></i>
            <span>Time Off Tracking</span>
          </div>
          <div class="feature-item">
            <i class="bi bi-chat-dots"></i>
            <span>Team Collaboration</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import swal from "sweetalert";
import axios from "axios";

export default {
  props: ["baseURL"],
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
      role: "",
      loading: false,
    };
  },
  methods: {
    async signup(e) {
      e.preventDefault();
      this.loading = true;

      const user = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        password: this.password,
        role: this.role,
      };

      await axios
        .post(`${this.baseURL}users/signUp`, user)
        .then((res) => {
          localStorage.setItem("token", res.data.token);
          this.$emit("fetchData");
          this.$router.push({ name: 'Home' });
        })
        .catch((err) => {
          swal({
            text: "Unable to sign you up!",
            icon: "error",
            closeOnClickOutside: false,
          });
          console.log(err);
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style scoped>
.auth-container {
  display: flex;
  min-height: 100vh;
  background-color: var(--airbnb-white);
}

.auth-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 48px;
  max-width: 600px;
  margin: 0 auto;
}

.auth-image {
  display: none;
  flex: 1;
  background: url('https://images.unsplash.com/photo-1600880292089-90a7e086ee0c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=987&q=80');
  background-size: cover;
  background-position: center;
  position: relative;
  color: white;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,56,92,0.8) 0%, rgba(189,30,89,0.8) 100%);
}

.image-content {
  position: relative;
  z-index: 2;
  padding: 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
}

.image-content h2 {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 24px;
  color: white;
}

.image-content p {
  font-size: 18px;
  line-height: 1.6;
  color: rgba(255,255,255,0.9);
  margin-bottom: 32px;
}

.features-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  background-color: rgba(255,255,255,0.1);
  border-radius: 12px;
  padding: 12px 16px;
}

.feature-item i {
  font-size: 24px;
  margin-right: 12px;
}

.feature-item span {
  font-size: 16px;
  font-weight: 500;
}

#logo {
  height: 50px;
  object-fit: contain;
}

.auth-title {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 8px;
  text-align: center;
  color: var(--airbnb-dark);
}

.auth-subtitle {
  text-align: center;
  color: var(--airbnb-light);
  font-size: 18px;
  margin-bottom: 32px;
}

.auth-form {
  margin-bottom: 32px;
}

.form-group {
  margin-bottom: 24px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  font-size: 16px;
  color: var(--airbnb-dark);
}

.input-with-icon {
  position: relative;
}

.input-with-icon i {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  color: var(--airbnb-light);
}

.form-control {
  height: 56px;
  font-size: 16px;
  padding-left: 48px;
  border-radius: 12px;
  border: 1px solid #DDDDDD;
  transition: all 0.3s ease;
}

.form-control:focus {
  box-shadow: 0 0 0 3px rgba(255,56,92,0.2);
  border-color: var(--airbnb-primary);
}

.password-strength {
  display: flex;
  align-items: center;
  margin-top: 8px;
}

.strength-indicator {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin-right: 8px;
}

.strength-indicator.weak {
  background-color: #ff3b30;
}

.strength-indicator.medium {
  background-color: #ffcc00;
}

.strength-indicator.strong {
  background-color: #34c759;
}

.strength-text {
  font-size: 14px;
  color: var(--airbnb-dark);
}

.form-check-label {
  font-size: 15px;
  color: var(--airbnb-light);
  font-weight: 500;
}

.terms-link {
  color: var(--airbnb-primary);
  font-weight: 600;
}

.form-check-input {
  margin-top: 0.2rem;
}

.btn-lg {
  height: 56px;
  font-size: 18px;
  font-weight: 600;
  border-radius: 12px;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid #DDDDDD;
}

.divider span {
  padding: 0 16px;
  color: var(--airbnb-light);
  font-size: 15px;
  font-weight: 500;
}

.oauth-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
}

.btn-oauth {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  border: 1px solid #DDDDDD;
  border-radius: 12px;
  padding: 12px;
  font-size: 16px;
  font-weight: 600;
  color: var(--airbnb-dark);
  transition: all 0.3s ease;
}

.btn-oauth:hover {
  background-color: var(--airbnb-bg);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.btn-oauth i {
  font-size: 20px;
  margin-right: 8px;
}

.oauth-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.auth-footer {
  font-size: 16px;
  color: var(--airbnb-light);
}

.signin-link {
  color: var(--airbnb-primary);
  font-weight: 600;
}

@media (min-width: 992px) {
  .auth-image {
    display: block;
  }
  
  .auth-card {
    padding: 48px 64px;
  }
}

@media (max-width: 768px) {
  .auth-card {
    padding: 32px 24px;
  }
  
  .oauth-buttons {
    flex-direction: column;
  }
  
  .auth-title {
    font-size: 28px;
  }
  
  .form-row {
    display: flex;
    flex-direction: column;
  }
  
  .form-row .col-md-6 {
    width: 100%;
    max-width: 100%;
    flex: 0 0 100%;
  }
}
</style>