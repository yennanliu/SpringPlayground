<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-10 col-12">
        <h1 class="font-weight-bold">Job: {{ job.id }}</h1>
        <h2 class="font-weight-bold mt-3">Detail:</h2>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Job ID</th>
              <th>Name</th>
              <th>State</th>
              <th>Start Time</th>
              <th>End Time</th>
              <th>Duration</th>
              <th>Flink Job Link</th>
            </tr>
          </thead>
          <tbody>
            <td>{{ job.id }}</td>
            <td>{{ job.jobId }}</td>
            <td>{{ job.name }}</td>
            <td>{{ job.state }}</td>
            <td>{{ job.startTime }}</td>
            <td>{{ job.endTime }}</td>
            <td>{{ job.duration }}</td>
            <!--
              http://localhost:8081/#/job/running/da174a6766a7d930054d566d508f2103/overview
            -->
            <td>
              <a :href="`${getFlinkJobLink(job)}`" target="_blank">
                View Job
              </a>
            </td>
          </tbody>
        </table>
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
      job: {},
    };
  },
  props: ["baseURL", "jobs"],
  methods: {
    toLowerCase(input) {
      if (input == null) {
        return ""; // or return null; depending on your use case
      }
      return input.toLowerCase();
    },

    getFlinkJobLink(job) {
      if (!job) {
        return null;
      }
      return (
        "http://localhost:8081/#/job/" +
        this.toLowerCase(job.state) +
        "/" +
        job.jobId
      );
    },

    async getJob() {
      await axios
        .get(`http://localhost:9999/job/${this.$route.params.id}`)
        .then((res) => {
          console.log("${this.$route.params.id} = " + this.$route.params.id);
          this.job = res.data;
          console.log(">>> (getJob) this.job = " + JSON.stringify());
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getJob();
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
      