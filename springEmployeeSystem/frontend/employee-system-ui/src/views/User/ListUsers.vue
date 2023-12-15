<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <!-- <h4 class="pt-3">{{ category.categoryName }}</h4> -->
        <h1>User List</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <!-- <img
        v-show="len == 0"
        class="img-fluid"
        src="../../assets/sorry.jpg"
        alt="Sorry"
      /> -->
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
import UserBox from "../../components/User/UserBox";
var axios = require("axios");
export default {
  name: "ListUsers",
  data() {
    return {
      id: null,
      users: null,
      len: 0,
      msg: null,
    };
  },
  components: { UserBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      // fetch users
      await axios
        .get("http://localhost:9998/" + "users/")
        .then((res) => (this.users = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    //this.id = this.$route.params.id;
    this.fetchData();
    console.log(">>> this.users = " + this.users);
  },
};
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

h5 {
  font-family: "Roboto", sans-serif;
  color: #686868;
  font-weight: 300;
}
</style>
