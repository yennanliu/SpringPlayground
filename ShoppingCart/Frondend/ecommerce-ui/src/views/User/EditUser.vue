<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit User</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form v-if="user">
          <!-- <div class="form-group">
            <label>Category</label>
            <select class="form-control" v-model="product.categoryId" required>
              <option
                v-for="category of categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.categoryName }}
              </option>
            </select>
          </div> -->
          <div class="form-group">
            <label>firstName</label>
            <input
              type="text"
              class="form-control"
              v-model="user.firstName"
              required
            />
          </div>
          <div class="form-group">
            <label>lastName</label>
            <input
              type="text"
              class="form-control"
              v-model="user.lastName"
              required
            />
          </div>
          <div class="form-group">
            <label>role</label>
            <input
              type="text"
              class="form-control"
              v-model="user.role"
              required
            />
          </div>
          <div class="form-group">
            <label>Email</label>
            <input
              type="text"
              class="form-control"
              v-model="user.email"
              required
            />
          </div>
          <button type="button" class="btn btn-primary" @click="editUser">
            Submit
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>
  
  <script>
var axios = require("axios");
import swal from "sweetalert";

export default {
  data() {
    return {
      user: null,
    };
  },
  //props: ["baseURL", "categories"],
  props: ["baseURL"],
  methods: {
    async editUser() {
      axios
        .post("http://localhost:9999/" + "user/updateUser/", this.user)
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
    this.token = localStorage.getItem("token");
    this.getUser();
    this.id = this.$route.params.id;
    console.log("this.id  = " + this.id);
    console.log("this.users  = " + JSON.stringify(this.users));
  },
};
</script>
  
  <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
  