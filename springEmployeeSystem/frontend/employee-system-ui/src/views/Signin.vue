<!-- 
    https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Signin.vue
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

      <h1 class="auth-title">Log in</h1>
      
      <form @submit="signin" class="auth-form">
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
            placeholder="Enter your password"
            required
          />
        </div>
        
        <small class="form-text text-muted mb-4">
          By continuing, you agree to the Employee System's Terms of Service and Privacy Policy.
        </small>
        
        <button type="submit" class="btn btn-primary btn-block">
          Log in
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
          Don't have an account?
          <router-link :to="{ name: 'Signup' }" class="signup-link">
            Sign up
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
  name: "Signin",
  props: ["baseURL"],
  data() {
    return {
      email: null,
      password: null,
      loading: null,
    };
  },
  methods: {
    async signin(e) {
      e.preventDefault();
      this.loading = true;

      const user = {
        email: this.email,
        password: this.password,
      };

      await axios
        .post(`${this.baseURL}users/signIn`, user)
        .then((res) => {
          localStorage.setItem("token", res.data.token);
          this.$emit("fetchData");
          this.$router.push({ name: "Home" });
        })
        .catch((err) => {
          swal({
            text: "Unable to Log you in!",
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
  mounted() {
    this.loading = false;
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
  max-width: 450px;
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

.signup-link {
  color: var(--airbnb-primary);
  font-weight: 600;
}

.auth-footer {
  font-size: 14px;
  color: var(--airbnb-light);
}
</style>