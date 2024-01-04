<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Job List</h1>
        <h5>{{ msg }}</h5>
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
import JobBox from "../../components/Job/JobBox";
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
  components: { JobBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      // fetch users
      await axios
        .get("http://localhost:9999/" + "job/")
        .then((res) => {
          this.jobs = res.data;
          console.log(
            ">>> (fetchData) this.jobs = " + JSON.stringify(this.jobs)
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
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
    