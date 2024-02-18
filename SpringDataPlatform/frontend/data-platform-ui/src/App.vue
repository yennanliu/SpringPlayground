<template>
  <div>
    <Navbar v-if="!['Signup', 'Signin'].includes($route.name)" />
    <div style="min-height: 60vh">
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
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
