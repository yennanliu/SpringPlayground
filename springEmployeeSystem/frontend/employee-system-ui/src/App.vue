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
  --airbnb-primary: #FF385C;
  --airbnb-secondary: #00A699;
  --airbnb-dark: #222222;
  --airbnb-light: #717171;
  --airbnb-bg: #F7F7F7;
  --airbnb-white: #FFFFFF;
  --border-radius: 12px;
  --shadow: 0 4px 8px rgba(0,0,0,0.08);
  --transition: all 0.3s ease;
}

body {
  background-color: var(--airbnb-white);
  font-size: 16px;
  line-height: 1.5;
}

#app {
  font-family: 'Circular', -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--airbnb-dark);
  line-height: 1.5;
}

h1, h2, h3, h4, h5, h6 {
  color: var(--airbnb-dark);
  font-weight: 700;
  margin-bottom: 16px;
}

h1 { font-size: 42px; }
h2 { font-size: 32px; }
h3 { font-size: 24px; }
h4 { font-size: 20px; }
h5 { font-size: 18px; }
h6 { font-size: 16px; }

p {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 16px;
}

.btn-primary {
  background-color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
  border-radius: var(--border-radius);
  font-weight: 600;
  transition: var(--transition);
  padding: 12px 24px;
  font-size: 16px;
}

.btn-primary:hover {
  background-color: #E31C5F;
  border-color: #E31C5F;
  transform: translateY(-2px);
}

.card {
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--shadow);
  border: none;
  transition: var(--transition);
}

.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

a {
  color: var(--airbnb-dark);
  transition: var(--transition);
  font-weight: 500;
}

a:hover {
  color: var(--airbnb-primary);
  text-decoration: none;
}

.form-control {
  border-radius: var(--border-radius);
  padding: 12px 16px;
  border: 1px solid #DDDDDD;
  font-size: 16px;
  height: auto;
}

.form-control:focus {
  box-shadow: 0 0 0 2px rgba(255,56,92,0.2);
  border-color: var(--airbnb-primary);
}

.container {
  padding: 0 24px;
}

@media (min-width: 768px) {
  .container {
    padding: 0 40px;
  }
  
  body {
    font-size: 17px;
  }
}

@media (min-width: 1128px) {
  .container {
    padding: 0 80px;
  }
}

/* Accessibility improvements */
:focus {
  outline: 3px solid rgba(255,56,92,0.4);
  outline-offset: 2px;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #bbb;
}
</style>
