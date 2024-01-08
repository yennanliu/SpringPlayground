<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <!--    Logo-->
    <router-link class="navbar-brand" :to="{ name: 'Home' }">
      <img id="logo" src="../assets/icon2.png" />
    </router-link>

    <!--    Burger Button-->
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
      <!--      DropDowns-->
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
            Flink Jar
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Home' }"
              >Home</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'ListJar' }"
              >Jar List</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'AddJar' }"
              >Add Jar</router-link
            >
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
            Flink Jobs
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'ListJob' }"
              >List Job</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'AddJob' }"
              >Submit new Job</router-link
            >
            <router-link class="dropdown-item" :to="{ name: 'AddSqlJob' }"
              >Submit new SQL Job</router-link
            >
            <!-- Zeppelin -->
            <router-link class="dropdown-item" :to="{ name: 'Zeppelin' }"
              >Zeppelin Home</router-link
            >
            <!-- zeppelinApp -->
            <router-link class="dropdown-item" :to="{ name: 'ZeppelinApp' }"
              >Zeppelin App</router-link
            >
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
            <router-link class="dropdown-item" :to="{ name: 'Admin' }"
              >Admin</router-link
            >
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signin' }"
              >Log In</router-link
            >
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signup' }"
              >Sign Up</router-link
            >
            <a class="dropdown-item" v-if="token" href="" @click="signout"
              >Sign Out</a
            >
          </div>
        </li>

        <!-- <li class="nav-item">
          <router-link class="nav-link text-light" :to="{ name: 'Order' }"
            >Orders</router-link
          >
        </li> -->

        <!-- <li class="nav-item">
          <div id="cart">
            <span id="nav-cart-count">{{ cartCount }}</span>
            <router-link class="text-light" :to="{ name: 'Cart' }"
              ><i class="fa fa-shopping-cart" style="font-size: 36px"></i
            ></router-link>
          </div>
        </li> -->
      </ul>
    </div>
  </nav>
</template>

<script>
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/components/Navbar.vue

import swal from "sweetalert";
export default {
  name: "Navbar",
  props: ["cartCount"],
  data() {
    return {
      token: null,
    };
  },
  methods: {
    signout() {
      localStorage.removeItem("token");
      this.token = null;
      this.$router.push({ name: "Home" });
      swal({
        text: "Logged you out. Visit Again",
        icon: "success",
        closeOnClickOutside: false,
      });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
  },
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
