<template>
  <nav class="navbar navbar-expand-lg">
    <!-- Logo -->
    <router-link class="navbar-brand" :to="{ name: 'Home' }">
      <div class="logo-container">
        <img id="logo" src="../assets/icon2.png" alt="Logo" />
      </div>
    </router-link>

    <!-- Burger Button-->
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
      <!-- Navigation Items -->
      <ul class="navbar-nav ml-auto">
        <!-- Employee Dropdown -->
        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Employee
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Home' }">Home</router-link>
            <router-link class="dropdown-item" :to="{ name: 'User' }">User</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Vacation' }">Vacation</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Checkin' }">Checkin</router-link>
          </div>
        </li>

        <!-- Department Dropdown -->
        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Department
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Department' }">Department</router-link>
            <router-link class="dropdown-item" :to="{ name: 'Ticket' }">Ticket</router-link>
          </div>
        </li>

        <!-- Auth Dropdown -->
        <li class="nav-item dropdown">
          <a
            class="nav-link dropdown-toggle auth-dropdown"
            href="#"
            id="navbarDropdown"
            role="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            <i class="bi bi-person-circle"></i>
            <span class="ml-2">Account</span>
          </a>
          <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
            <router-link class="dropdown-item" :to="{ name: 'Admin' }">Admin</router-link>
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signin' }"
            >Log In</router-link>
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signup' }"
            >Sign Up</router-link>
            <a class="dropdown-item" v-if="token" href="" @click="signout">Sign Out</a>
          </div>
        </li>
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
.navbar {
  padding: 16px 24px;
  background-color: var(--airbnb-white);
  border-bottom: 1px solid #EBEBEB;
}

@media (min-width: 768px) {
  .navbar {
    padding: 16px 40px;
  }
}

@media (min-width: 1128px) {
  .navbar {
    padding: 16px 80px;
  }
}

.logo-container {
  display: flex;
  align-items: center;
}

#logo {
  height: 32px;
  object-fit: contain;
}

.navbar-nav {
  align-items: center;
}

.nav-link {
  color: var(--airbnb-dark);
  font-weight: 500;
  padding: 10px 16px;
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.nav-link:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
}

.dropdown-menu {
  border-radius: var(--border-radius);
  box-shadow: 0 6px 20px rgba(0,0,0,0.2);
  border: 1px solid #EBEBEB;
  padding: 8px 0;
  min-width: 240px;
}

.dropdown-item {
  padding: 12px 16px;
  font-size: 14px;
  transition: var(--transition);
}

.dropdown-item:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
}

.auth-dropdown {
  display: flex;
  align-items: center;
  background-color: var(--airbnb-white);
  border: 1px solid #DDDDDD;
  border-radius: 21px;
  padding: 5px 12px;
}

.auth-dropdown:hover {
  box-shadow: var(--shadow);
}

.auth-dropdown i {
  font-size: 1.25rem;
}

.navbar-toggler {
  border: 1px solid #DDDDDD;
  padding: 4px 8px;
  border-radius: 4px;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='30' height='30' viewBox='0 0 30 30'%3e%3cpath stroke='rgba(0, 0, 0, 0.5)' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}
</style>
