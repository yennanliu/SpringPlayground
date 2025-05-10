<template>
  <nav class="uber-navbar">
    <div class="navbar-container">
      <!-- Logo -->
      <div class="navbar-logo">
        <router-link :to="{ name: 'Home' }">
          <img id="logo" src="../assets/icon3.png" alt="Logo" />
        </router-link>
      </div>

      <!-- Menu Items -->
      <div class="navbar-menu">
        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">Cluster</span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'ListCluster' }">Cluster List</router-link>
            <router-link :to="{ name: 'AddCluster' }">Add Cluster</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">Zeppelin</span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'Zeppelin' }">Zeppelin Home</router-link>
            <router-link :to="{ name: 'ListNotebook' }">Notebook List</router-link>
            <router-link :to="{ name: 'AddNotebook' }">Add Notebook</router-link>
            <router-link :to="{ name: 'NotebookConsole' }">Notebook Console</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">Flink Jobs</span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'ListJob' }">Job List</router-link>
            <router-link :to="{ name: 'AddJob' }">Submit new Jar Job</router-link>
            <router-link :to="{ name: 'AddSqlJob' }">Submit new SQL Job</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">Flink Jar</span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'Home' }">Home</router-link>
            <router-link :to="{ name: 'ListJar' }">Jar List</router-link>
            <router-link :to="{ name: 'AddJar' }">Add Jar</router-link>
          </div>
        </div>
      </div>

      <!-- Auth Section -->
      <div class="navbar-auth">
        <div v-if="!token" class="auth-buttons">
          <router-link :to="{ name: 'Signin' }" class="btn-login">Log in</router-link>
          <router-link :to="{ name: 'Signup' }" class="btn-signup">Sign up</router-link>
        </div>
        <div v-else class="auth-buttons">
          <router-link :to="{ name: 'Admin' }" class="btn-admin">Admin</router-link>
          <a href="#" @click.prevent="signout" class="btn-signout">Sign out</a>
        </div>
      </div>

      <!-- Mobile Toggle -->
      <div class="mobile-toggle">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>
  </nav>
</template>

<script>
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/components/Navbar.vue

import swal from "sweetalert";
export default {
  name: "Navbar",
  props: ["cartCount"],
  data() {
    return {
      token: null,
    };
  },
  methods: {
    signout() {
      localStorage.removeItem("token");
      this.token = null;
      this.$router.push({ name: "Home" });
      swal({
        text: "Logged you out. Visit Again",
        icon: "success",
        closeOnClickOutside: false,
      });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
  },
};
</script>

<style scoped>
.uber-navbar {
  background-color: #000000;
  color: #ffffff;
  height: 80px;
  position: sticky;
  top: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.1);
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.navbar-logo {
  flex: 0 0 auto;
}

#logo {
  height: 30px;
  width: auto;
}

.navbar-menu {
  display: flex;
  align-items: center;
  margin-left: 30px;
}

.navbar-item {
  position: relative;
  margin: 0 15px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
}

.dropdown-toggle {
  color: #ffffff;
  padding: 8px 0;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #ffffff;
  min-width: 180px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  z-index: 1;
  border-radius: 8px;
  overflow: hidden;
  top: 40px;
  left: -20px;
}

.dropdown-content a {
  color: #000000;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  font-size: 14px;
  transition: background-color 0.2s;
}

.dropdown-content a:hover {
  background-color: #f5f5f5;
}

.navbar-item:hover .dropdown-content {
  display: block;
}

.navbar-auth {
  display: flex;
  align-items: center;
}

.auth-buttons {
  display: flex;
  align-items: center;
}

.btn-login, .btn-admin {
  color: #ffffff;
  margin-right: 15px;
  padding: 8px 16px;
  font-weight: 500;
}

.btn-signup, .btn-signout {
  background-color: #ffffff;
  color: #000000;
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 500;
}

.mobile-toggle {
  display: none;
  flex-direction: column;
  cursor: pointer;
}

.mobile-toggle span {
  width: 24px;
  height: 2px;
  background-color: #ffffff;
  margin: 3px 0;
  transition: all 0.3s ease;
}

@media (max-width: 992px) {
  .navbar-menu {
    display: none;
  }
  
  .mobile-toggle {
    display: flex;
  }
  
  .navbar-auth {
    display: none;
  }
}
</style>
