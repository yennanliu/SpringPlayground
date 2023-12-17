<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Users</h4>
        <router-link
          id="add-user"
          :to="{ name: 'AddUser' }"
          v-show="$route.name == 'AdminUser'"
        >
          <button class="btn">Add a new user</button>
        </router-link>
      </div>
    </div>
    <div class="row">
      <div
        v-for="user of users"
        :key="user.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <UserBox :user="user"> </UserBox>
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
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

#add-user {
  float: right;
  font-weight: 500;
}
</style>