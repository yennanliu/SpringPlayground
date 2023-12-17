<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link> |
      <router-link to="/users">Users</router-link> |
      <router-link to="/admin/users">AdminUsers</router-link> |
      <router-link to="/departments">Departments</router-link> |
      <router-link to="/admin/departments">AdminDepartments</router-link> 
    </nav>
    <router-view />
  </div>
</template>

<script>
var axios = require("axios");
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
