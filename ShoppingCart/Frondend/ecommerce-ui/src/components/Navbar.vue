<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <!-- Logo -->
    <router-link class="navbar-brand" :to="{ name: 'Home' }">
      <img id="logo" src="../assets/icon2.png" />
    </router-link>

    <!-- Burger Button -->
    <button
      class="navbar-toggler"
      type="button"
      data-toggle="collapse"
      data-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <!-- Search Bar -->
      <form class="form-inline ml-auto mr-auto" @submit.prevent="searchProduct">
        <div class="input-group">
          <input
            v-model="searchQuery"
            size="100"
            type="text"
            class="form-control"
            placeholder="Search Items"
            aria-label="Search"
            aria-describedby="basic-addon1"
          />
          <div class="input-group-prepend">
            <button class="input-group-text" id="search-button-navbar" type="submit">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                fill="currentColor"
                class="bi bi-search"
                viewBox="0 0 16 16"
              >
                <path
                  d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
                />
              </svg>
            </button>
          </div>
        </div>
      </form>

      <!-- DropDowns -->
      <ul class="navbar-nav ml-auto">
        <li class="nav-item dropdown">
          <a
            class="nav-link text-light dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Browse
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Home' }">Home</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Product' }">Product</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Category' }">Category</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Cart' }">Cart</router-link>
            <router-link class="dropdown-item" v-if="!token" :to="{ name: 'Signin' }">Wishlist</router-link>
            <router-link class="dropdown-item" v-else :to="{ name: 'Wishlist' }">Wishlist</router-link>
          </div>
        </li>

        <li class="nav-item dropdown">
          <a
            class="nav-link text-light dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            LogIn/SignUp
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Admin' }">Admin</router-link>
            <router-link class="dropdown-item" :to="{ name: 'User' }">User</router-link>
            <router-link class="dropdown-item" v-if="!token" :to="{ name: 'Signin' }">Log In</router-link>
            <router-link class="dropdown-item" v-if="!token" :to="{ name: 'Signup' }">Sign Up</router-link>
            <a class="dropdown-item" v-if="token" href="" @click="signout">Sign Out</a>
          </div>
        </li>

        <li class="nav-item">
          <router-link class="nav-link text-light" :to="{ name: 'Order' }">Orders</router-link>
        </li>

        <li class="nav-item">
          <div id="cart">
            <span id="nav-cart-count">{{ cartCount }}</span>
            <router-link class="text-light" :to="{ name: 'Cart' }">
              <i class="fa fa-shopping-cart" style="font-size: 36px"></i>
            </router-link>
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';

export default {
  name: 'Navbar',
  props: ['cartCount'],
  data() {
    return {
      token: null,
      searchQuery: ''
    };
  },
  methods: {
    signout() {
      localStorage.removeItem('token');
      this.token = null;
      this.$emit('resetCartCount');
      this.$router.push({ name: 'Home' });
      swal({
        text: 'Logged you out. Visit Again',
        icon: 'success',
        closeOnClickOutside: false,
      });
    },
    async searchProduct() {
      try {
        console.log(">>> (searchProduct) this.searchQuery = " + this.searchQuery)
        const response = await axios.get("http://localhost:9999" + `/search/api/?query=${this.searchQuery}`);
        // Handle the search results here
        console.log(">>> response.data = " + JSON.stringify(response.data));
        this.$emit('searchResults', response.data); // Emitting the results to the parent component
      } catch (error) {
        console.error('Error searching for products:', error);
      }
    }
  },
  mounted() {
    this.token = localStorage.getItem('token');
  }
};
</script>

<style scoped>
#logo {
  width: 150px;
  margin-left: 20px;
  margin-right: 20px;
}

.nav-link {
  color: rgba(255, 255, 255);
}

#search-button-navbar {
  background-color: #febd69;
  border-color: #febd69;
  border-top-right-radius: 2px;
  border-bottom-right-radius: 2px;
}
#nav-cart-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 15px;
  height: 15px;
  font-size: 15px;
  margin-left: 10px;
}
#cart {
  position: relative;
}
</style>