<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Job List</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Job Name</th>
            <th>State</th>
            <th>Detail</th>
            <!-- Add more columns if needed -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in jobs" :key="job.id">
            <td>{{ job.id }}</td>
            <td>{{ job.name }}</td>
            <td>{{ job.state }}</td>
            <td>
              <router-link :to="`/jobs/show/${job.id}`">
                Job Detail
              </router-link>
            </td>
            <!-- Add more columns if needed -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
//import JobBox from "../../components/Job/JobBox";
var axios = require("axios");

export default {
  name: "ListJob",
  data() {
    return {
      id: null,
      jobs: [],
      len: 0,
      msg: null,
    };
  },
  //components: { JobBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      try {
        // "http://localhost:9999/job/"
        const response = await axios.get(`${this.baseURL}/job/`);
        this.jobs = response.data;
      } catch (error) {
        console.error(error);
      }
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
h1,
h5 {
  font-family: "Roboto", sans-serif;
  color: #484848;
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  color: #212529;
}

.table th,
.table td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}
</style>
