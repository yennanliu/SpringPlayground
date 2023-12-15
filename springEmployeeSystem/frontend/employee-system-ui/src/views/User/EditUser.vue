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
        <form v-if="product">
          <div class="form-group">
            <label>Department</label>
            <select class="form-control" v-model="user.departmentId" required>
              <option
                v-for="department of departments"
                :key="department.id"
                :value="department.id"
              >
                {{ department.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Name</label>
            <input
              type="text"
              class="form-control"
              v-model="user.name"
              required
            />
          </div>
          <div class="form-group">
            <label>email</label>
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
      product: null,
      products: [],
    };
  },
  //props: ["baseURL", "products", "categories"],
  props: ["baseURL", "categories"],
  methods: {
    async editUser() {
      axios
        .post(
          "http://localhost:9998/" + "users/update/" + this.id,
          this.product
        )
        .then((res) => {
          console.log(res);
          //sending the event to parent to handle
          this.$emit("fetchData");
          this.$router.push({ name: "AdminProduct" });
          swal({
            text: "User Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log("err", err));
    },

    async getProduct() {
      //fetch categories
      await axios
        .get(this.baseURL + "product/")
        .then((res) => {
          //console.log("res = " + JSON.stringify(res))
          //console.log("res.data = " + JSON.stringify(res.data))
          console.log(
            "this.products.find = " +
              JSON.stringify(
                res.data.find((product) => product.id == this.$route.params.id)
              )
          );
          // use this approach for now
          this.product = res.data.find(
            (product) => product.id == this.$route.params.id
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    // if (!localStorage.getItem("token")) {
    //   this.$router.push({ name: "Signin" });
    //   return;
    // }
    this.getProduct();
    this.id = this.$route.params.id;
    console.log("this.id  = " + this.id);
    console.log("this.products  = " + JSON.stringify(this.products));
    console.log("this.categories  = " + JSON.stringify(this.categories));
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
