<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link> |
      <router-link to="/category">Category</router-link> |
      <router-link to="/wishlist">Wishlist</router-link> |
      <router-link to="/product">Product</router-link> |
      <router-link to="/admin/category">Category (admin)</router-link> |
      <router-link to="/admin/wishlist">Wishlist (admin)</router-link> |
      <router-link to="/admin/product">Product (admin)</router-link> |
      <router-link to="/order">Order</router-link> |
      <router-link to="/cart">Cart</router-link> |
      <router-link to="/signup">Signup</router-link> |
      <router-link to="/signin">Signin</router-link> 
    </nav>

    <!-- 
      https://youtu.be/VZ1NV7EHGJw?si=FtsSuMndmHLiBwsc&t=710 

      delcare global variable via router view
      -> so baseURL, categories are visible to ALL views
    -->
    <router-view :baseURL="baseURL" :categories="categories"> </router-view>
  </div>
</template>

<script>
import axios from "axios";

export default {
  // components: {},
  data() {
    return {
      baseURL: "http://localhost:9999/", // this baseURL will be read by all views
      products: null,
      categories: null,
      token: null,
    };
  },
  methods: {
    async fetchData() {
      // fetch products
      await axios
        .get(this.baseURL + "product/")
        .then((res) => (this.products = res.data))
        .catch((err) => console.log(err));

      //fetch categories
      await axios
        .get(this.baseURL + "category/")
        .then((res) => (this.categories = res.data))
        .catch((err) => console.log(err));
    },
  },

  mounted() {
    /**
     *  // NOTE!!! via this method call, we can get products, categories
     *     when launch FE app (App.vue is imported to main.js as main FE entry point),
     *     so all other views (e.g. Product, EditProduct, Category, EditCategory...)
     *     can use products, categories directly via
     *
     *     props: ["baseURL", "categories", "products"],
     *
     *     this.categories
     *     this.products
     *
     *     ..
     *
     *     via above trick, we can simplify our code, and make logic more clear, simple
     */
     this.token = localStorage.getItem('token');
     console.log("this.token = " + this.token)
     this.fetchData();
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
