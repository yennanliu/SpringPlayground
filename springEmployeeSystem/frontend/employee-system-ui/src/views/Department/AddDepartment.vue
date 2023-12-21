<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add new Department</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <!-- <div class="form-group">
            <label>Category</label>
            <select class="form-control" v-model="departmentId" required>
              <option
                v-for="department of departments"
                :key="department.id"
                :value="department.id"
              >
                {{ departments.name }}
              </option>
            </select>
          </div> -->
          <div class="form-group">
            <label>Name</label>
            <input type="text" class="form-control" v-model="name" required />
          </div>
          <button type="button" class="btn btn-primary" @click="addDepartment">
            Submit
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>
  
  <script>
import swal from "sweetalert";
import axios from "axios";

export default {
  data() {
    return {
      id: null,
      departmentId: null,
      name: null,
      departments: [],
      department: null
    };
  },
  props: ["baseURL", "products"],
  methods: {
    async addDepartment() {
      const newProduct = {
        id: this.id,
        name: this.name,
      };

      await axios({
        method: "post",
        url: "http://localhost:9998/" + "dep/add",
        data: JSON.stringify(newProduct),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "AdminDepartment" });
          swal({
            text: "Product Added Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log(err));
    },
  },

  mounted() {},
};
</script>
  
  <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
  