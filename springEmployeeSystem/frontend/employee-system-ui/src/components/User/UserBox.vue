<template>
  <div class="user-card">
    <router-link :to="{ name: 'ShowUserDetails', params: { id: user.id } }" class="user-link">
      <div class="user-header">
        <div class="user-avatar">
          <span>{{ getInitials(user.firstName, user.lastName) }}</span>
        </div>
        <div class="user-status">
          <span class="status-indicator online"></span>
          <span class="status-text">Online</span>
        </div>
      </div>
      <div class="user-info">
        <h3 class="user-name">{{ user.firstName }} {{ user.lastName }}</h3>
        <p class="user-email">{{ user.email }}</p>
        <div class="user-details">
          <div class="detail-item">
            <i class="bi bi-building"></i>
            <span>Marketing</span>
          </div>
          <div class="detail-item">
            <i class="bi bi-geo-alt"></i>
            <span>New York</span>
          </div>
        </div>
        <div class="user-role">
          <span class="role-badge">{{ user.role }}</span>
        </div>
      </div>
    </router-link>
    
    <div class="user-actions" v-show="$route.name == 'AdminUser'">
      <router-link 
        :to="{ name: 'EditUser', params: { id: user.id } }"
        class="btn btn-sm btn-outline-primary"
      >
        <i class="bi bi-pencil-square mr-2"></i> Edit
      </router-link>
      <button class="btn btn-sm btn-icon ml-2">
        <i class="bi bi-three-dots-vertical"></i>
      </button>
    </div>
  </div>
</template>

<!-- NOTE !!!

  Will only show product edit button when route name is "AdminProduct"

  e.g. 
        <router-link
          id="edit-product"
          :to="{ name: 'EditProduct', params: { id: product.id } }"
          v-show="$route.name == 'AdminProduct'"
        >
          Edit
        </router-link>
  -->

<script>
var axios = require("axios");
import swal from "sweetalert";
export default {
  name: "UserBox",
  props: ["user"],
  data() {
    return {
      users: [],
    };
  },
  methods: {
    getInitials(firstName, lastName) {
      return (
        (firstName ? firstName.charAt(0).toUpperCase() : '') + 
        (lastName ? lastName.charAt(0).toUpperCase() : '')
      );
    },
    
    ShowUserDetails() {
      console.log(
        "(ShowUserDetails) this.$route.params.id = " + this.$route.params.id
      );
      this.$router.push({
        name: "ShowUserDetails",
        arams: { id: this.$route.params.id },
      });
    },

    async editUser() {
      axios
        .post("http://localhost:9998/" + "users/update/", this.user)
        .then((res) => {
          console.log(res);
          //sending the event to parent to handle
          this.$emit("fetchData");
          this.$router.push({ name: "AdminUser" });
          swal({
            text: "User Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log("err", err));
    },
  },
};
</script>

<style scoped>
.user-card {
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  overflow: hidden;
  transition: var(--transition);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.user-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.user-link {
  display: flex;
  flex-direction: column;
  padding: 24px;
  color: var(--airbnb-dark);
  text-decoration: none;
  flex: 1;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--airbnb-primary) 0%, #FF7E82 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(255, 56, 92, 0.25);
}

.user-status {
  display: flex;
  align-items: center;
  padding: 6px 12px;
  background-color: rgba(240, 242, 245, 0.8);
  border-radius: 20px;
}

.status-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 6px;
}

.status-indicator.online {
  background-color: #44CC11;
}

.status-indicator.offline {
  background-color: #ddd;
}

.status-text {
  font-size: 14px;
  color: var(--airbnb-dark);
  font-weight: 500;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--airbnb-dark);
}

.user-email {
  color: var(--airbnb-light);
  font-size: 16px;
  margin-bottom: 16px;
  word-break: break-all;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-item i {
  font-size: 18px;
  color: var(--airbnb-light);
  margin-right: 10px;
}

.detail-item span {
  font-size: 15px;
  color: var(--airbnb-dark);
}

.user-role {
  margin-bottom: 8px;
}

.role-badge {
  display: inline-block;
  padding: 6px 16px;
  background-color: rgba(255, 56, 92, 0.1);
  color: var(--airbnb-primary);
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
}

.user-actions {
  padding: 16px 24px;
  border-top: 1px solid #EBEBEB;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.btn-outline-primary {
  color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
  font-weight: 600;
  padding: 8px 16px;
}

.btn-outline-primary:hover {
  background-color: var(--airbnb-primary);
  color: white;
}

.btn-icon {
  width: 36px;
  height: 36px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: 1px solid #EBEBEB;
  background-color: white;
  color: var(--airbnb-light);
  transition: var(--transition);
}

.btn-icon:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-dark);
}

.btn-icon i {
  font-size: 16px;
}
</style>
