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
            <label>Name</label>
            <input
              type="text"
              class="form-control"
              v-model="user.name"
              required
            />
          </div>
          <!-- <div class="form-group">
            <label>Description</label>
            <input
              type="text"
              class="form-control"
              v-model="product.description"
              required
            />
          </div> -->
          <!-- <div class="form-group">
            <label>ImageURL</label>
            <input
              type="url"
              class="form-control"
              v-model="product.imageURL"
              required
            />
          </div> -->
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
      product: null,
      products: [],
    };
  },
  //props: ["baseURL", "categories"],
  props: ["baseURL"],
  methods: {
    async editUser() {
      axios
        .post("http://localhost:9999/" + "user/update/" + this.id, this.user)
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
        .get(this.baseURL + "user/all")
        .then((res) => {
          console.log(
            "this.user.find = " +
              JSON.stringify(
                res.data.find((user) => user.id == this.$route.params.id)
              )
          );
          // use this approach for now
          this.user = res.data.find((user) => user.id == this.$route.params.id);
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
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
  