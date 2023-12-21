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
        <h2 class="font-weight-bold">
          Name : {{ user.firstName + " " + user.lastName }}
        </h2>
        <h6 class="font-weight">ID : {{ user.id }}</h6>
        <h6 class="font-weight">Email : {{ user.email }}</h6>
        <h6 class="font-weight">Department : {{ user.departementId }}</h6>
        <h6 class="font-weight">Manager : {{ user.managerId }}</h6>
        <h6 class="font-weight">Role : {{ user.role }}</h6>

        Vacation>>> :
        <ul>
          <li v-for="vacation in this.userVacations" :key="vacation.id">
            ID: {{ vacation.id }} |  {{ vacation.startDate }} |  {{ vacation.endDate }}
          </li>
        </ul>

        <!-- <ul>
          <li v-for="user in users" :key="user.id">
            {{ user.name }} (ID: {{ user.id }})
          </li>
        </ul> -->
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
      vacations: [],
      userVacations: [],
    };
  },
  props: ["baseURL", "users"],
  methods: {
    ListUsers() {
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
    async getUserVacations() {
      //fetch categories
      await axios
        .get("http://localhost:9998/vacation/")
        .then((res) => {
          this.vacations = res.data;
        })
        .catch((err) => console.log(err));

      // filter get vacation with user id
      this.userVacations = this.vacations.filter(
        (vacation) => vacation.userId == this.$route.params.id
      );
      console.log(
        ">>> this.userVacations  = " + JSON.stringify(this.userVacations)
      );
    },
  },
  mounted() {
    console.log(">>> this.$route.params.id = " + this.$route.params.id);
    this.getUser();
    this.getUserVacations();
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
