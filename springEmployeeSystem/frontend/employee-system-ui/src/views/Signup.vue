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
      
      <h1 class="auth-title">Sign up</h1>
      
      <form @submit="signup" class="auth-form">
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="firstname">First Name</label>
            <input
              type="text"
              id="firstname"
              class="form-control"
              v-model="firstName"
              placeholder="Enter first name"
              required
            />
          </div>
          <div class="form-group col-md-6">
            <label for="lastname">Last Name</label>
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
        
        <div class="form-group">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            class="form-control"
            v-model="email"
            placeholder="Enter your email"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            class="form-control"
            v-model="password"
            placeholder="Create a password"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="role">Role</label>
          <select id="role" class="form-control" v-model="role" required>
            <option value="" disabled selected>Select a role</option>
            <option value="admin">Admin</option>
            <option value="manager">Manager</option>
            <option value="employee">Employee</option>
          </select>
        </div>
        
        <small class="form-text text-muted mb-4">
          By signing up, you agree to the Employee System's Terms of Service and Privacy Policy.
        </small>
        
        <button type="submit" class="btn btn-primary btn-block">
          Sign up
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
        <span>or</span>
      </div>
      
      <div class="oauth-buttons">
        <button class="btn btn-outline-dark btn-block mb-2">
          <i class="bi bi-google mr-2"></i> Continue with Google
        </button>
        <button class="btn btn-outline-dark btn-block">
          <i class="bi bi-microsoft mr-2"></i> Continue with Microsoft
        </button>
      </div>
      
      <div class="auth-footer mt-4">
        <p class="text-center">
          Already have an account?
          <router-link :to="{ name: 'Signin' }" class="signin-link">
            Log in
          </router-link>
        </p>
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
          this.$router.push({ name: "Home" });
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
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--airbnb-bg);
  padding: 16px;
}

.auth-card {
  background-color: var(--airbnb-white);
  border-radius: var(--border-radius);
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  width: 100%;
  max-width: 550px;
  padding: 32px;
}

#logo {
  height: 40px;
  object-fit: contain;
}

.auth-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 24px;
  text-align: center;
}

.auth-form {
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  font-size: 14px;
}

.form-control {
  height: 48px;
  font-size: 16px;
}

.btn-primary {
  height: 48px;
  font-size: 16px;
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  margin-bottom: 24px;
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
  font-size: 14px;
}

.oauth-buttons {
  margin-bottom: 24px;
}

.btn-outline-dark {
  border-color: #DDDDDD;
  color: var(--airbnb-dark);
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

.btn-outline-dark:hover {
  background-color: var(--airbnb-bg);
  border-color: var(--airbnb-light);
  color: var(--airbnb-dark);
}

.signin-link {
  color: var(--airbnb-primary);
  font-weight: 600;
}

.auth-footer {
  font-size: 14px;
  color: var(--airbnb-light);
}
</style>