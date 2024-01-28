<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Flink Jobs</h4>
        <router-link
          id="add-department"
          :to="{ name: 'AddJob' }"
          v-show="$route.name == 'AdminJob'"
        >
          <button class="btn">Add a new Job</button>
        </router-link>
      </div>
    </div>
    <div class="row">
      <div
        v-for="job of jobs"
        :key="job.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <JobBox :job="job"> </JobBox>
      </div>
    </div>
  </div>
</template>

<script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

import JobBox from "../../components/Job/JobBox";
var axios = require("axios");

export default {
  name: "Jar",
  components: { JobBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      jobs: [],
    };
  },
  methods: {
    async getJobs() {
      await axios
        // http://localhost:9999/job/
        .get(`${this.baseURL}/job/`)
        .then((res) => (this.jobs = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getJobs();
  },
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
