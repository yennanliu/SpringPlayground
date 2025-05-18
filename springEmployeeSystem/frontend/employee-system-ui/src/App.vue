<template>
  <div>
    <Navbar v-if="!['Signup', 'Signin'].includes($route.name)" />
    <div style="min-height: 70vh">
      <!-- 
      https://youtu.be/VZ1NV7EHGJw?si=FtsSuMndmHLiBwsc&t=710 

      delcare global variable via router view
      -> so baseURL, categories are visible to ALL views
      -->
      <router-view :baseURL="baseURL" @fetchData="fetchData"> </router-view>
    </div>
    <!-- <Footer v-if="!['Signup', 'Signin'].includes($route.name)" /> -->
    <Footer v-if="!['Signup', 'Signin'].includes($route.name)" />
  </div>
</template>

<script>
var axios = require("axios");
import Navbar from "./components/Navbar.vue";
import Footer from "./components/Footer.vue";
export default {
  data() {
    return {
      baseURL: "http://localhost:9998/",
      users: null,
      departments: null,
      key: 0,
      token: null,
      cartCount: 0,
    };
  },

  components: { Footer, Navbar },
  methods: {
    async fetchData() {
      // get users
      await axios
        .get(this.baseURL + "users/")
        .then((res) => (this.users = res.users))
        .catch((err) => console.log(err));
    },
    mounted() {
      this.fetchData();
    },
  },
};
</script>


<style>
:root {
  --airbnb-primary: #FF5A5F;
  --airbnb-secondary: #00A699;
  --airbnb-dark: #484848;
  --airbnb-light: #767676;
  --airbnb-bg: #F7F7F7;
  --airbnb-white: #FFFFFF;
  --border-radius: 8px;
  --shadow: 0 2px 4px rgba(0,0,0,0.08);
  --transition: all 0.2s ease;
}

body {
  background-color: var(--airbnb-white);
}

#app {
  font-family: 'Circular', -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--airbnb-dark);
  line-height: 1.43;
}

h1, h2, h3, h4, h5, h6 {
  color: var(--airbnb-dark);
  font-weight: 600;
}

.btn-primary {
  background-color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
  border-radius: var(--border-radius);
  font-weight: 500;
  transition: var(--transition);
  padding: 8px 16px;
}

.btn-primary:hover {
  background-color: #FF7E82;
  border-color: #FF7E82;
}

.card {
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--shadow);
  border: none;
  transition: var(--transition);
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

a {
  color: var(--airbnb-dark);
  transition: var(--transition);
}

a:hover {
  color: var(--airbnb-primary);
  text-decoration: none;
}

.form-control {
  border-radius: var(--border-radius);
  padding: 10px 12px;
  border: 1px solid #EBEBEB;
}

.form-control:focus {
  box-shadow: 0 0 0 2px rgba(255,90,95,0.2);
  border-color: var(--airbnb-primary);
}

.container {
  padding: 0 24px;
}

@media (min-width: 768px) {
  .container {
    padding: 0 40px;
  }
}

@media (min-width: 1128px) {
  .container {
    padding: 0 80px;
  }
}
</style>
