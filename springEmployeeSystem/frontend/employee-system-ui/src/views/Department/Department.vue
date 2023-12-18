<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Departments</h4>
        <router-link
          id="add-user"
          :to="{ name: 'AddDepartment' }"
          v-show="$route.name == 'AdminDepartment'"
        >
          <button class="btn">Add a new Department</button>
        </router-link>
      </div>
    </div>
    <div class="row">
      <div
        v-for="department of departments"
        :key="department.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <DepartmentBox :department="department"> </DepartmentBox>
      </div>
    </div>
  </div>
</template>

<script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

import DepartmentBox from "../../components/Department/DepartmentBox";
var axios = require("axios");

export default {
  name: "Department",
  components: { DepartmentBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      departments: [],
    };
  },
  methods: {
    async getDepartments() {
      await axios
        .get("http://localhost:9998/dep/")
        .then((res) => {
          this.departments = res.data;
          console.log(">>> this.departments = " + JSON.stringify(this.departments))
        })
        .catch((err) => console.log("getDepartments err = " + err));
    },
  },
  mounted() {
    this.getDepartments();
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
