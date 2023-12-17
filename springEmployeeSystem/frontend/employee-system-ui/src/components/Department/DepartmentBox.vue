<template>
  <div class="card h-100 w-100">
    <div class="card-body">
      <router-link :to="{ name: 'ShowDetails', params: { id: department.id } }"
        ><h5 class="card-title">Name : {{ department.name }}</h5></router-link
      >
      <router-link
        id="edit-department"
        :to="{ name: 'EditDepartment', params: { id: department.id } }"
        v-show="$route.name == 'AdminDepartment'"
      >
        Edit
      </router-link>
    </div>
  </div>
</template>
  
  <!-- NOTE !!!
  
    Will only show product edit button when route name is "AdminProduct"
  
    e.g. 
          <router-link
            id="edit-product"
            :to="{ name: 'EditProduct', params: { id: product.id } }"
            v-show="$route.name == 'AdminProduct'"
          >
            Edit
          </router-link>
    -->
  
  <script>
var axios = require("axios");
import swal from "sweetalert";
export default {
  name: "DepartmentBox",
  props: ["department"],
  data() {
    return {
      users: [],
    };
  },
  methods: {
    ShowDepartmentDetails() {
      console.log(
        "(ShowDepartmentDetails) this.$route.params.id = " +
          this.$route.params.id
      );
      this.$router.push({
        name: "ShowDepartmentDetails",
        arams: { id: this.$route.params.id },
      });
    },

    async editUser() {
      axios
        .post("http://localhost:9998/dep/", this.department)
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
  },
};
</script>
  
  <style scoped>
.embed-responsive .card-img-top {
  object-fit: cover;
}

a {
  text-decoration: none;
}

.card-title {
  color: #484848;
  font-size: 1.1rem;
  font-weight: 400;
}

.card-title:hover {
  font-weight: bold;
}

.card-text {
  font-size: 0.9rem;
}

#edit-user {
  float: right;
}
</style>
  