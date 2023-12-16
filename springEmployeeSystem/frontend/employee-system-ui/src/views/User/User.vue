<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">User Profile</h4>
      </div>
    </div>
    <div class="row">
      <UserBox :user="user"> </UserBox>
    </div>
  </div>
</template>
  
  <script>
import UserBox from "../../components/User/UserBox";
var axios = require("axios");

export default {
  name: "User",
  components: { UserBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      user: null,
    };
  },
  methods: {
    async getUser() {
      console.log("this.token = " + this.token);
      axios
        // http://localhost:9999/user/userProfile?token=8230d006-5271-49fc-84fd-28b80b3b66e3
        .get(this.baseURL + `/user/userProfile?token=${this.token}`)
        .then((response) => {
          // Handle the response data here
          this.user = response.data;
          console.log(response.data);
        })
        .catch((error) => {
          // Handle errors
          console.error("getUser error :", error);
        });
    },
  },
  mounted() {
    // login first if can't get token
    if (!localStorage.getItem("token")) {
      this.$router.push({ name: "Signin" });
      return;
    }
    // get token
    this.token = localStorage.getItem("token");
    this.getUser();
  },
};
</script>
  
  <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

#add-product {
  float: right;
  font-weight: 500;
}
</style>
  