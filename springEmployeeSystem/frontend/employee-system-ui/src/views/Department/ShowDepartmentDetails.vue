<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <!-- <div class="col-md-4 col-12">
          <img :src="product.imageURL" :alt="product.name" class="img-fluid" />
        </div> -->
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h6 class="font-weight-bold">Id : {{ department.id }}</h6>
        <h6 class="font-weight-bold">Name : {{ department.name }}</h6>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>
  
  <script>
//import swal from "sweetalert";
import axios from "axios";

export default {
  data() {
    return {
      department: {},
      id: null,
      token: null,
    };
  },
  props: ["baseURL", "users"],
  methods: {
    ListDepartments() {
      axios.get(`http://localhost:9998/dep/`).then(
        (response) => {
          if (response.status === 200) {
            this.$router.push("/department");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },

    // TODO : fix with filter from product list
    async getDepartment() {
      // fetch users
      await axios
        .get(`http://localhost:9998/dep/${this.$route.params.id}`)
        .then((res) => (this.department = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    console.log(">>> this.$route.params.id = " + this.$route.params.id);
    //this.ListUsers();
    this.getDepartment();
    // this.token = localStorage.getItem("token");
    // console.log("this.token = " + this.token);
  },
};
</script>
  
  <style>
.category {
  font-weight: 400;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}

#add-to-cart-button {
  background-color: #febd69;
}

#wishlist-button {
  background-color: #b9b9b9;
  border-radius: 0;
}

#show-cart-button {
  background-color: #131921;
  color: white;
  border-radius: 0;
}
</style>
  