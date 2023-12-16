<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <!-- <div class="col-md-4 col-12">
        <img :src="product.imageURL" :alt="product.name" class="img-fluid" />
      </div> -->
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h4>{{ user.name }}</h4>
        <!-- <h6 class="category font-italic">{{ category.categoryName }}</h6> -->
        <h6 class="font-weight-bold">
          Name : {{ user.firstName + " " + user.lastName }}
        </h6>
        <h6 class="font-weight-bold">Email : {{ user.email }}</h6>
        <h6 class="font-weight-bold">Department : {{ user.departementId }}</h6>
        <h6 class="font-weight-bold">Manager : {{ user.managerId }}</h6>
        <p>Role : {{ user.role }}</p>
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
      user: {},
      id: null,
      token: null,
    };
  },
  props: ["baseURL", "users"],
  methods: {
    ListUsers() {
      //   if (!this.token) {
      //     swal({
      //       text: "Please log in first!",
      //       icon: "error",
      //     });
      //     return;
      //   }
      axios.get(`http://localhost:9998/users/`).then(
        (response) => {
          if (response.status === 200) {
            this.$router.push("/user");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },

    // TODO : fix with filter from product list
    async getUser() {
      // fetch users
      await axios
        .get(`http://localhost:9998/users/${this.$route.params.id}`)
        .then((res) => (this.user = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    console.log(">>> this.$route.params.id = " + this.$route.params.id);
    //this.ListUsers();
    this.getUser();
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
