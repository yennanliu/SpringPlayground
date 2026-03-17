<template>
  <div id="app">
    <Navbar v-if="!['Signup', 'Signin'].includes(routeName)" />
    <div style="min-height: 75vh">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Navbar from "./components/Navbar.vue"

const route = useRoute()
const routeName = computed(() => route.name)
</script>

<style>
/* Global styles */
body {
  margin: 0;
  padding: 0;
}

#app {
  font-family: 'UberMove', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333333;
  background-color: #ffffff;
}

h1, h2, h3, h4, h5, h6 {
  font-weight: 600;
  margin-bottom: 12px;
}

a {
  text-decoration: none;
  color: #000000;
}

.btn {
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-primary {
  background-color: #000000;
  color: #ffffff;
  border: none;
}

.btn-primary:hover {
  background-color: #333333;
}

.btn-secondary {
  background-color: #eeeeee;
  color: #000000;
  border: none;
}

.btn-secondary:hover {
  background-color: #dddddd;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

/* Responsive text utilities */
@media (max-width: 768px) {
  h1 {
    font-size: 32px;
  }
  h2 {
    font-size: 24px;
  }
}
</style>
