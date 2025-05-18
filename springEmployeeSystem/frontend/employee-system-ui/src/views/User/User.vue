<template>
  <div class="user-container">
    <div class="container">
      <div class="section-header">
        <div>
          <h1 class="section-title">Users</h1>
          <p class="section-subtitle">Manage your team members</p>
        </div>
        <router-link
          :to="{ name: 'AddUser' }"
          v-show="$route.name == 'AdminUser'"
          class="btn btn-primary btn-add"
        >
          <i class="bi bi-plus-lg mr-2"></i>Add User
        </router-link>
      </div>
      
      <div class="search-filter-bar">
        <div class="search-bar">
          <i class="bi bi-search search-icon"></i>
          <input type="text" class="form-control" placeholder="Search users by name, email or role..." />
        </div>
        <div class="filter-controls">
          <div class="dropdown mr-3">
            <button class="btn btn-filter dropdown-toggle" type="button" id="departmentDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="bi bi-building mr-2"></i>Department
            </button>
            <div class="dropdown-menu" aria-labelledby="departmentDropdown">
              <a class="dropdown-item active" href="#">All Departments</a>
              <a class="dropdown-item" href="#">Engineering</a>
              <a class="dropdown-item" href="#">Marketing</a>
              <a class="dropdown-item" href="#">Sales</a>
              <a class="dropdown-item" href="#">Human Resources</a>
            </div>
          </div>
          
          <div class="dropdown">
            <button class="btn btn-filter dropdown-toggle" type="button" id="roleDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="bi bi-person-badge mr-2"></i>Role
            </button>
            <div class="dropdown-menu" aria-labelledby="roleDropdown">
              <a class="dropdown-item active" href="#">All Roles</a>
              <a class="dropdown-item" href="#">Admin</a>
              <a class="dropdown-item" href="#">Manager</a>
              <a class="dropdown-item" href="#">Employee</a>
            </div>
          </div>
        </div>
      </div>
      
      <div class="view-controls">
        <button class="btn btn-view active">
          <i class="bi bi-grid-3x3-gap-fill"></i>
        </button>
        <button class="btn btn-view">
          <i class="bi bi-list-ul"></i>
        </button>
        <div class="sort-by ml-auto">
          <span>Sort by:</span>
          <select class="form-control form-control-sm">
            <option>Name (A-Z)</option>
            <option>Name (Z-A)</option>
            <option>Role</option>
            <option>Date Added</option>
          </select>
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
        <button class="btn btn-primary mt-3">
          <i class="bi bi-arrow-repeat mr-2"></i>Refresh
        </button>
      </div>
      
      <div class="pagination-controls" v-if="users.length > 0">
        <nav aria-label="Page navigation">
          <ul class="pagination">
            <li class="page-item disabled">
              <a class="page-link" href="#" aria-label="Previous">
                <i class="bi bi-chevron-left"></i>
              </a>
            </li>
            <li class="page-item active"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item">
              <a class="page-link" href="#" aria-label="Next">
                <i class="bi bi-chevron-right"></i>
              </a>
            </li>
          </ul>
        </nav>
        <div class="results-counter">
          Showing <span>{{ users.length }}</span> of <span>{{ users.length }}</span> users
        </div>
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
  padding: 48px 0;
  background-color: var(--airbnb-bg);
  min-height: calc(100vh - 150px);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

.section-title {
  font-size: 36px;
  font-weight: 800;
  color: var(--airbnb-dark);
  margin: 0 0 8px 0;
}

.section-subtitle {
  font-size: 18px;
  color: var(--airbnb-light);
  margin: 0;
}

.btn-add {
  font-weight: 600;
  display: flex;
  align-items: center;
}

.search-filter-bar {
  display: flex;
  margin-bottom: 32px;
  gap: 16px;
  flex-wrap: wrap;
}

.search-bar {
  flex: 1;
  position: relative;
  min-width: 250px;
}

.search-icon {
  position: absolute;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--airbnb-light);
  font-size: 18px;
}

.search-bar .form-control {
  padding-left: 54px;
  height: 56px;
  border-radius: 28px;
  border: 1px solid #DDDDDD;
  font-size: 17px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.search-bar .form-control:focus {
  box-shadow: 0 0 0 2px rgba(255,56,92,0.2), 0 2px 8px rgba(0,0,0,0.05);
  border-color: var(--airbnb-primary);
}

.filter-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.btn-filter {
  background-color: white;
  border: 1px solid #DDDDDD;
  border-radius: 24px;
  padding: 14px 20px;
  height: 56px;
  display: flex;
  align-items: center;
  color: var(--airbnb-dark);
  font-weight: 600;
  font-size: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.btn-filter i {
  font-size: 18px;
  margin-right: 8px;
}

.btn-filter:hover {
  border-color: var(--airbnb-light);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.view-controls {
  display: flex;
  margin-bottom: 24px;
  align-items: center;
}

.btn-view {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  background-color: transparent;
  border: 1px solid #DDDDDD;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: var(--airbnb-light);
  transition: var(--transition);
}

.btn-view i {
  font-size: 18px;
}

.btn-view.active {
  background-color: var(--airbnb-primary);
  color: white;
  border-color: var(--airbnb-primary);
}

.sort-by {
  display: flex;
  align-items: center;
}

.sort-by span {
  font-size: 15px;
  margin-right: 12px;
  color: var(--airbnb-light);
}

.sort-by .form-control {
  width: auto;
  border-radius: 8px;
  border: 1px solid #DDDDDD;
  padding: 8px 16px;
  color: var(--airbnb-dark);
  font-weight: 500;
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 28px;
  margin-bottom: 40px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  text-align: center;
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
}

.empty-icon {
  font-size: 64px;
  color: var(--airbnb-light);
  margin-bottom: 24px;
}

.empty-state h3 {
  font-size: 24px;
  margin-bottom: 12px;
  font-weight: 700;
  color: var(--airbnb-dark);
}

.empty-state p {
  font-size: 18px;
  color: var(--airbnb-light);
  max-width: 450px;
  margin-bottom: 24px;
}

.pagination-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 32px;
}

.pagination {
  margin-bottom: 0;
}

.page-link {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 4px;
  border-radius: 50% !important;
  border: 1px solid #DDDDDD;
  color: var(--airbnb-dark);
  font-weight: 600;
  transition: var(--transition);
}

.page-item.active .page-link {
  background-color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
}

.page-link:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
}

.results-counter {
  color: var(--airbnb-light);
  font-size: 15px;
}

.results-counter span {
  font-weight: 600;
  color: var(--airbnb-dark);
}

.dropdown-menu {
  border-radius: var(--border-radius);
  box-shadow: 0 8px 28px rgba(0,0,0,0.2);
  border: none;
  padding: 12px 0;
  min-width: 220px;
}

.dropdown-item {
  padding: 14px 20px;
  font-size: 16px;
  transition: var(--transition);
}

.dropdown-item.active {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
  font-weight: 600;
}

.dropdown-item:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
}

@media (max-width: 992px) {
  .search-filter-bar {
    flex-direction: column;
  }
  
  .section-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .pagination-controls {
    flex-direction: column;
    gap: 20px;
  }
  
  .results-counter {
    text-align: center;
  }
}

@media (max-width: 768px) {
  .users-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 30px;
  }
  
  .view-controls {
    flex-wrap: wrap;
    gap: 16px;
  }
  
  .sort-by {
    margin-left: 0;
    width: 100%;
    margin-top: 16px;
    justify-content: flex-end;
  }
}
</style>