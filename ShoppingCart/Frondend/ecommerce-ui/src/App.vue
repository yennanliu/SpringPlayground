<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link> |
      <router-link to="/admin/category">Category</router-link> |
      <router-link to="/admin/category/add">Add</router-link> |
      <router-link to="/admin/wishlist">Wishlist</router-link> |
      <router-link to="/admin/product">Product</router-link>
    </nav>
    <router-view />
  </div>
</template>

<script>
import axios from "axios";

export default {
  // components: {},
  data() {
    return {
      baseURL: "http://localhost:9999/",
      products: [],
      categories: [],
    };
  },
  methods: {
    async fetchData() {
      await axios
        .get(this.baseURL + "category/")
        .then((res) => {
          this.categories = res.data;
        })
        .catch((err) => console.log("err : " + err));
    },
  },

  mounted() {
    this.fetchData;
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
