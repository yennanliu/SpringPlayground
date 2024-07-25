<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-5 col-12 pt-3 pt-md-0">
        <!-- User details section -->
        <h4>{{ user.name }}</h4>
        <h2 class="font-weight-bold">
          Name: {{ user.firstName + " " + user.lastName }}
        </h2>

        <h6 class="font-weight">ID: 
          <router-link :to="`/users/show/${user.id}`">
            {{ user.id }}
          </router-link>
        </h6>
        <h6 class="font-weight">Email: {{ user.email }}</h6>
        <h6 class="font-weight">Department: 
          <router-link :to="`/departments/show/${user.departmentId}`">
            {{ user.departmentId }}
          </router-link>
        </h6>
        <h6 class="font-weight">Manager: 
          <router-link :to="`/users/show/${user.managerId}`">
            {{ user.managerId }}
          </router-link>
        </h6>
        <h6 class="font-weight">Role: {{ user.role }}</h6>

        <h3 class="font-weight mt-3">Vacations:</h3>
        <!-- User vacations table -->
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Status</th>
              <th>Type</th>
              <th>View</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="vacation in userVacations" :key="vacation.id">
              <td>{{ vacation.id }}</td>
              <td>{{ vacation.startDate }}</td>
              <td>{{ vacation.endDate }}</td>
              <td>{{ vacation.status }}</td>
              <td>{{ vacation.type }}</td>
              <td>
                <router-link :to="`/vacation`"> View vacation </router-link>
              </td>
            </tr>
          </tbody>
        </table>

        <h3 class="font-weight mt-3">Subordinates:</h3>
        <!-- Subordinates table -->
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Department</th>
              <th>Manager</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="subordinate in subordinates" :key="subordinate.id">
              <td>{{ subordinate.id }}</td>
              <td>{{ subordinate.firstName }} {{ subordinate.lastName }}</td>
              <td>{{ subordinate.email }}</td>
              <td>{{ subordinate.role }}</td>
              <td>{{ subordinate.departmentId }}</td>
              <td>
                <router-link :to="`/users/show/${subordinate.managerId}`">
                  {{ subordinate.managerId }}
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- <div class="col-md-5 col-12 pt-3 pt-md-0 text-center">
        <img
          :src="user.photoUrl || 'https://via.placeholder.com/300'"
          alt="User Photo"
          style="max-width: 100%; max-height: 300px"
        />
      </div> -->

      <div class="col-md-5 col-12 pt-3 pt-md-0 text-center">
        <!-- User photo -->
        <img
          :src="user.photoUrl || require('./default_user_photo.png')"
          alt="User Photo"
          style="max-width: 100%; max-height: 300px"
        />
      </div>

      <div class="col-md-1"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      user: {},
      userVacations: [],
      subordinates: [],
    };
  },
  methods: {
    async getUser() {
      // Fetch user details
      await axios
        .get(`http://localhost:9998/users/${this.$route.params.id}`)
        .then((res) => (this.user = res.data))
        .catch((err) => console.log(err));
    },
    async getSubordinates() {
      // Fetch subordinates details
      await axios
        .get(
          `http://localhost:9998/users/subordinates/${this.$route.params.id}`
        )
        .then((res) => (this.subordinates = res.data))
        .catch((err) => console.log(err));
    },
    async getUserVacations() {
      // Fetch user vacations
      await axios
        .get("http://localhost:9998/vacation/")
        .then((res) => {
          this.userVacations = res.data.filter(
            (vacation) => vacation.userId == this.$route.params.id
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    // Fetch user, subordinates, and user vacations on component mount
    this.getUser();
    this.getSubordinates();
    this.getUserVacations();
  },
};
</script>

<style scoped>
/* Add scoped styles here if needed */
</style>