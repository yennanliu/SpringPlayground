<template>
  <div class="user-container">
    <div class="container">
      <div class="section-header">
        <h1 class="section-title">Users</h1>
        <!-- <router-link
          :to="{ name: 'AddUser' }"
          v-show="$route.name == 'AdminUser'"
          class="btn btn-primary"
        >
          <i class="bi bi-plus-lg mr-2"></i>Add User
        </router-link> -->
      </div>
      
      <div class="search-filter-bar">
        <div class="search-bar">
          <i class="bi bi-search search-icon"></i>
          <input type="text" class="form-control" placeholder="Search users..." />
        </div>
        <div class="dropdown">
          <button class="btn btn-filter dropdown-toggle" type="button" id="filterDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <i class="bi bi-funnel mr-2"></i>Filter
          </button>
          <div class="dropdown-menu" aria-labelledby="filterDropdown">
            <a class="dropdown-item" href="#">All Users</a>
            <a class="dropdown-item" href="#">Admin</a>
            <a class="dropdown-item" href="#">Employee</a>
            <a class="dropdown-item" href="#">Manager</a>
          </div>
        </div>
      </div>
      
      <div class="users-grid">
        <div
          v-for="user of users"
          :key="user.id"
          class="grid-item"
        >
          <UserBox :user="user"> </UserBox>
        </div>
      </div>
      
      <div v-if="users.length === 0" class="empty-state">
        <i class="bi bi-people empty-icon"></i>
        <h3>No users found</h3>
        <p>Try adjusting your search or filter to find what you're looking for.</p>
      </div>
    </div>
  </div>
</template>

<script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

import UserBox from "../../components/User/UserBox";
var axios = require("axios");

export default {
  name: "User",
  components: { UserBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      users: [],
    };
  },
  methods: {
    async getUsers() {
      await axios
        .get("http://localhost:9998/users/")
        .then((res) => (this.users = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getUsers();
  },

  // TODO : deal with token, login/logout
  // mounted(){
  //   if (this.$route.name=='AdminProduct' && !localStorage.getItem('token')) {
  //     this.$router.push({name : 'Signin'});
  //   }
  // }
};
</script>

<style scoped>
.user-container {
  padding: 40px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--airbnb-dark);
  margin: 0;
}

.search-filter-bar {
  display: flex;
  margin-bottom: 32px;
  gap: 16px;
}

.search-bar {
  flex: 1;
  position: relative;
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--airbnb-light);
}

.search-bar .form-control {
  padding-left: 44px;
  height: 48px;
  border-radius: 24px;
  border: 1px solid #DDDDDD;
}

.search-bar .form-control:focus {
  box-shadow: 0 0 0 2px rgba(255,90,95,0.2);
  border-color: var(--airbnb-primary);
}

.btn-filter {
  background-color: white;
  border: 1px solid #DDDDDD;
  border-radius: 24px;
  padding: 8px 16px;
  height: 48px;
  display: flex;
  align-items: center;
  color: var(--airbnb-dark);
}

.btn-filter:hover {
  border-color: var(--airbnb-light);
  box-shadow: var(--shadow);
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 64px 0;
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  color: var(--airbnb-light);
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 20px;
  margin-bottom: 8px;
  color: var(--airbnb-dark);
}

.empty-state p {
  font-size: 16px;
  color: var(--airbnb-light);
  max-width: 400px;
}

@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .search-filter-bar {
    flex-direction: column;
  }
  
  .users-grid {
    grid-template-columns: 1fr;
  }
}
</style>