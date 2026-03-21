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
          <span class="dropdown-toggle">
            Cluster
            <svg class="dropdown-arrow" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'ListCluster' }">Cluster List</router-link>
            <router-link :to="{ name: 'AddCluster' }">Add Cluster</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">
            Zeppelin
            <svg class="dropdown-arrow" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'Zeppelin' }">Zeppelin Home</router-link>
            <router-link :to="{ name: 'ListNotebook' }">Notebook List</router-link>
            <router-link :to="{ name: 'AddNotebook' }">Add Notebook</router-link>
            <router-link :to="{ name: 'NotebookConsole' }">Notebook Console</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">
            Flink Jobs
            <svg class="dropdown-arrow" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </span>
          <div class="dropdown-content">
            <router-link :to="{ name: 'ListJob' }">Job List</router-link>
            <router-link :to="{ name: 'AddJob' }">Submit new Jar Job</router-link>
            <router-link :to="{ name: 'AddSqlJob' }">Submit new SQL Job</router-link>
          </div>
        </div>

        <div class="navbar-item dropdown">
          <span class="dropdown-toggle">
            Flink Jar
            <svg class="dropdown-arrow" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </span>
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
          <a href="#" @click.prevent="handleSignout" class="btn-signout">Sign out</a>
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

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import swal from "sweetalert"
import { useAuthStore } from "@/stores"

const router = useRouter()
const authStore = useAuthStore()

const token = computed(() => authStore.token)

const handleSignout = () => {
  authStore.signout()
  router.push({ name: "Home" })
  swal({
    text: "Logged you out. Visit Again",
    icon: "success",
    closeOnClickOutside: false,
  })
}
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
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
  transition: opacity 0.2s ease;
}

#logo:hover {
  opacity: 0.8;
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
  display: inline-flex;
  align-items: center;
  gap: 6px;
  transition: color 0.2s ease;
}

.dropdown-toggle:hover {
  color: #f0c14b;
}

.dropdown-arrow {
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.navbar-item:hover .dropdown-arrow {
  transform: rotate(180deg);
}

.dropdown-content {
  position: absolute;
  background-color: #ffffff;
  min-width: 200px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  z-index: 1;
  border-radius: 12px;
  overflow: hidden;
  top: 45px;
  left: -20px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              transform 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              visibility 0.25s;
}

.navbar-item:hover .dropdown-content {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-content a {
  color: #000000;
  padding: 14px 20px;
  text-decoration: none;
  display: block;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  position: relative;
}

.dropdown-content a::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: #f0c14b;
  transform: scaleY(0);
  transition: transform 0.2s ease;
}

.dropdown-content a:hover {
  background-color: #f8f8f8;
  padding-left: 24px;
}

.dropdown-content a:hover::before {
  transform: scaleY(1);
}

.navbar-auth {
  display: flex;
  align-items: center;
}

.auth-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-login, .btn-admin {
  color: #ffffff;
  padding: 10px 20px;
  font-weight: 500;
  transition: color 0.2s ease;
  text-decoration: none;
}

.btn-login:hover, .btn-admin:hover {
  color: #f0c14b;
}

.btn-signup, .btn-signout {
  background-color: #ffffff;
  color: #000000;
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s ease;
  text-decoration: none;
}

.btn-signup:hover, .btn-signout:hover {
  background-color: #f0c14b;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(240, 193, 75, 0.3);
}

.mobile-toggle {
  display: none;
  flex-direction: column;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.mobile-toggle:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.mobile-toggle span {
  width: 24px;
  height: 2px;
  background-color: #ffffff;
  margin: 3px 0;
  transition: all 0.3s ease;
  border-radius: 2px;
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
