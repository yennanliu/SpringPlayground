<template>
  <div>
    <Navbar
      :cartCount="cartCount"
      @resetCartCount="resetCartCount"
      v-if="!['Signup', 'Signin'].includes($route.name)"
    />
    <div style="min-height: 60vh">
       <!-- 
      https://youtu.be/VZ1NV7EHGJw?si=FtsSuMndmHLiBwsc&t=710 

      delcare global variable via router view
      -> so baseURL, categories are visible to ALL views
      -->
      <router-view
        v-if="products && categories"
        :baseURL="baseURL"
        :products="products"
        :categories="categories"
        @fetchData="fetchData"
      >
      </router-view>
    </div>
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
      //baseURL: "https://limitless-lake-55070.herokuapp.com/",
      baseURL: "http://localhost:9999/",
      products: null,
      categories: null,
      key: 0,
      token: null,
      cartCount: 0,
    };
  },

  components: { Footer, Navbar },
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

      //fetch cart items
      if (this.token) {
        await axios.get(`${this.baseURL}cart/?token=${this.token}`).then(
          (response) => {
            if (response.status == 200) {
              // update cart
              this.cartCount = Object.keys(response.data.cartItems).length;
            }
          },
          (error) => {
            console.log(error);
          }
        );
      }
    },
    resetCartCount() {
      this.cartCount = 0;
    },
  },
  mounted() {
    /**
     *  // NOTE!!! via this.fetchData() call, we can get products, categories
     *
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

    this.token = localStorage.getItem("token");
    console.log("this.token = " + this.token);
    this.fetchData();
  },
};
</script>

<style>
html {
  overflow-y: scroll;
}
</style>