<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <!-- <h4 class="pt-3">{{ category.categoryName }}</h4> -->
        <h1>Department List</h1>
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
import DepartmentBox from "../../components/Department/DepartmentBox";
var axios = require("axios");
export default {
  name: "ListDepartment",
  data() {
    return {
      id: null,
      departments: [],
      len: 0,
      msg: null,
    };
  },
  components: { DepartmentBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      // fetch users
      await axios
        .get("http://localhost:9998/" + "dep/")
        .then((res) => {
          this.departments = res.data;
          console.log(
            ">>> (fetchData) this.departments = " +
              JSON.stringify(this.departments)
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    //this.id = this.$route.params.id;
    this.fetchData();
    //console.log(">>> (ListUsers) this.users = " + JSON.stringify(this.users));
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
  