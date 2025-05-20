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
            <router-link class="dropdown-item" :to="{ name: 'Home' }">
              <i class="bi bi-house-door mr-2"></i>Home
            </router-link>
            <router-link class="dropdown-item" :to="{ name: 'User' }">
              <i class="bi bi-people mr-2"></i>User
            </router-link>
            <router-link class="dropdown-item" :to="{ name: 'Vacation' }">
              <i class="bi bi-calendar-check mr-2"></i>Vacation
            </router-link>
            <router-link class="dropdown-item" :to="{ name: 'Checkin' }">
              <i class="bi bi-clock mr-2"></i>Checkin
            </router-link>
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
            <router-link class="dropdown-item" :to="{ name: 'Department' }">
              <i class="bi bi-building mr-2"></i>Department
            </router-link>
            <router-link class="dropdown-item" :to="{ name: 'Ticket' }">
              <i class="bi bi-ticket-perforated mr-2"></i>Ticket
            </router-link>
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
            <router-link class="dropdown-item" :to="{ name: 'Admin' }">
              <i class="bi bi-shield-lock mr-2"></i>Admin
            </router-link>
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signin' }"
            ><i class="bi bi-box-arrow-in-right mr-2"></i>Log In</router-link>
            <router-link
              class="dropdown-item"
              v-if="!token"
              :to="{ name: 'Signup' }"
            ><i class="bi bi-person-plus mr-2"></i>Sign Up</router-link>
            <a class="dropdown-item" v-if="token" href="" @click="signout">
              <i class="bi bi-box-arrow-left mr-2"></i>Sign Out
            </a>
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
  padding: 20px 24px;
  background-color: var(--airbnb-white);
  border-bottom: 1px solid #EBEBEB;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

@media (min-width: 768px) {
  .navbar {
    padding: 20px 40px;
  }
}

@media (min-width: 1128px) {
  .navbar {
    padding: 20px 80px;
  }
}

.logo-container {
  display: flex;
  align-items: center;
}

#logo {
  height: 40px;
  object-fit: contain;
}

.navbar-nav {
  align-items: center;
}

.nav-link {
  color: var(--airbnb-dark);
  font-weight: 600;
  font-size: 17px;
  padding: 12px 18px;
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.nav-link:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
}

.dropdown-menu {
  border-radius: var(--border-radius);
  box-shadow: 0 8px 28px rgba(0,0,0,0.2);
  border: none;
  padding: 12px 0;
  min-width: 260px;
  margin-top: 12px;
}

.dropdown-item {
  padding: 14px 18px;
  font-size: 16px;
  font-weight: 500;
  transition: var(--transition);
  display: flex;
  align-items: center;
}

.dropdown-item i {
  font-size: 18px;
  margin-right: 12px;
  color: var(--airbnb-light);
}

.dropdown-item:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
}

.dropdown-item:hover i {
  color: var(--airbnb-primary);
}

.auth-dropdown {
  display: flex;
  align-items: center;
  background-color: var(--airbnb-white);
  border: 1px solid #DDDDDD;
  border-radius: 24px;
  padding: 8px 16px;
}

.auth-dropdown:hover {
  box-shadow: var(--shadow);
}

.auth-dropdown i {
  font-size: 1.5rem;
  color: var(--airbnb-dark);
}

.navbar-toggler {
  border: 1px solid #DDDDDD;
  padding: 8px 12px;
  border-radius: 8px;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' width='30' height='30' viewBox='0 0 30 30'%3e%3cpath stroke='rgba(0, 0, 0, 0.5)' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}
</style>
