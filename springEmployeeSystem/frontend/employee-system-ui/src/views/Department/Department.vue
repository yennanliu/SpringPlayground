<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit Department</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form v-if="department">
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
            <label>name</label>
            <input
              type="text"
              class="form-control"
              v-model="department.name"
              required
            />
          </div>
          <button type="button" class="btn btn-primary" @click="editDepartment">
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
      department: null,
      departments: [],
    };
  },
  //props: ["baseURL", "products", "categories"],
  props: ["baseURL"],
  methods: {
    async editDepartment() {
      axios
        .post("http://localhost:9998/dep/update/", this.department)
        .then((res) => {
          console.log(res);
          //sending the event to parent to handle
          this.$emit("fetchData");
          this.$router.push({ name: "AdminDepartment" });
          swal({
            text: "Department Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log("err", err));
    },

    async getDepartment() {
      //fetch categories
      await axios
        .get("http://localhost:9998/" + "dep/")
        .then((res) => {
          // use this approach for now
          this.department = res.data.find(
            (department) => department.id == this.$route.params.id
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
