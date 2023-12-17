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
            <select class="form-control" v-model="user.id" required>
              <option
                v-for="user of users"
                :key="user.id"
                :value="user.id"
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
            <label>Email</label>
            <input
              type="text"
              class="form-control"
              v-model="user.email"
              required
            />
          </div>
          <!-- <div class="form-group">
            <label>ImageURL</label>
            <input
              type="url"
              class="form-control"
              v-model="product.imageURL"
              required
            />
          </div> -->

          <!-- <div class="form-group">
            <label>Price</label>
            <input
              type="number"
              class="form-control"
              v-model="product.price"
              required
            />
          </div> -->

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
    };
  },
  //props: ["baseURL", "products", "categories"],
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
  },
  mounted() {
    // if (!localStorage.getItem("token")) {
    //   this.$router.push({ name: "Signin" });
    //   return;
    // }

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
