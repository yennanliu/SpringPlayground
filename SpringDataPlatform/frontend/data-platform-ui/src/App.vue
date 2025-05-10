<template>
  <div id="app">
    <Navbar v-if="!['Signup', 'Signin'].includes($route.name)" />
    <div style="min-height: 75vh">
      <!-- 
      https://youtu.be/VZ1NV7EHGJw?si=FtsSuMndmHLiBwsc&t=710 

      delcare global variable via router view
      -> so baseURL, categories are visible to ALL views
      -->
      <router-view :baseURL="baseURL" @fetchData="fetchData"> </router-view>
    </div>
    <!-- <Footer v-if="!['Signup', 'Signin'].includes($route.name)" /> -->
  </div>
</template>

<script>
var axios = require("axios");
import Navbar from "./components/Navbar.vue";
export default {
  data() {
    return {
      baseURL: "http://localhost:8081", //nginx port : 8081, BE port : 9999, 9998, "http://localhost:9999",
      users: null,
      departments: null,
      key: 0,
      token: null,
      cartCount: 0,
    };
  },

  components: { Navbar },
  methods: {
    async fetchData() {
      // get jars
      await axios
        .get(this.baseURL + "jar/")
        .then((res) => (this.jars = res.jars))
        .catch((err) => console.log(err));
    },
    mounted() {
      this.fetchData();
    },
  },
};
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
