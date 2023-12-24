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
            <label>Email</label>
            <input
              type="text"
              class="form-control"
              v-model="user.email"
              required
            />
          </div>

          <label>Department</label>
          <select class="form-control" v-model="user.departementId" required>
            <option
              v-for="department of departments"
              :key="department.id"
              :value="department.id"
            >
              ID : {{ department.id }} Name : {{ department.name }}
            </option>
          </select>

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
      users: [],
      departments: [],
    };
  },
  props: ["baseURL"],
  methods: {
    async editUser() {
      axios
        .post("http://localhost:9998/users/update/", this.user)
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
      //fetch categories
      await axios
        .get("http://localhost:9998/" + "users/")
        .then((res) => {
          // use this approach for now
          this.user = res.data.find((user) => user.id == this.$route.params.id);
        })
        .catch((err) => console.log(err));
    },

    async getDepartments() {
      //fetch categories
      await axios
        .get("http://localhost:9998/" + "dep/")
        .then((res) => {
          // use this approach for now
          this.departments = res.data;
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getDepartments();
    this.getUser();
    this.id = this.$route.params.id;
    console.log("this.id  = " + this.id);
    console.log("this.users  = " + JSON.stringify(this.users));

    // TODO : fix why can filter product via id
    //this.product = this.products.find((product) => product.id == this.id);
    //console.log(">>> this.product  = " + JSON.stringify(this.product));
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
