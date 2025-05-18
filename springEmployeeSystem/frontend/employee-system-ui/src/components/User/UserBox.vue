<template>
  <div class="user-card">
    <router-link :to="{ name: 'ShowUserDetails', params: { id: user.id } }" class="user-link">
      <div class="user-avatar">
        <span>{{ getInitials(user.firstName, user.lastName) }}</span>
      </div>
      <div class="user-info">
        <h3 class="user-name">{{ user.firstName }} {{ user.lastName }}</h3>
        <p class="user-email">{{ user.email }}</p>
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
        <i class="bi bi-pencil-square mr-1"></i> Edit
      </router-link>
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
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.user-link {
  display: flex;
  flex-direction: column;
  padding: 20px;
  color: var(--airbnb-dark);
  text-decoration: none;
  flex: 1;
}

.user-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background-color: var(--airbnb-primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 16px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--airbnb-dark);
}

.user-email {
  color: var(--airbnb-light);
  font-size: 14px;
  margin-bottom: 12px;
  word-break: break-all;
}

.user-role {
  margin-bottom: 8px;
}

.role-badge {
  display: inline-block;
  padding: 4px 12px;
  background-color: var(--airbnb-bg);
  color: var(--airbnb-primary);
  border-radius: 16px;
  font-size: 12px;
  font-weight: 600;
}

.user-actions {
  padding: 12px 20px;
  border-top: 1px solid #EBEBEB;
  display: flex;
  justify-content: flex-end;
}

.btn-outline-primary {
  color: var(--airbnb-primary);
  border-color: var(--airbnb-primary);
}

.btn-outline-primary:hover {
  background-color: var(--airbnb-primary);
  color: white;
}
</style>
