<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-10 col-12">
        <h1 class="font-weight-bold">Cluster: {{ cluster.id }}</h1>
        <h2 class="font-weight-bold mt-3">Detail:</h2>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Url</th>
              <th>Port</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{{ cluster.id }}</td>
              <td>{{ cluster.url }}</td>
              <td>{{ cluster.port }}</td>
              <td>{{ cluster.status }}</td>
            </tr>
          </tbody>
        </table>

        <!-- Add "Test Cluster" button -->
        <button class="btn btn-primary" @click="pingCluster">
          Test Cluster Connection
        </button>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import swal from "sweetalert";

export default {
  data() {
    return {
      cluster: {},
      clusterStatus: {},
    };
  },
  props: ["baseURL"],
  methods: {
    async getCluster() {
      try {
        const response = await axios.get(
          `http://localhost:9999/cluster/${this.$route.params.id}`
        );
        this.cluster = response.data;
      } catch (error) {
        console.error(error);
      }
    },

    // Add the "Test Cluster" method
    async pingCluster() {
      const pingReq = {
        id: this.$route.params.id,
      };

      console.log(">>> pingReq = " + JSON.stringify(pingReq));

      await axios({
        method: "post",
        url: "http://localhost:9999/cluster/ping",
        data: JSON.stringify(pingReq),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(
            ">>> pingCluster success  " + JSON.stringify(res.data.success)
          );
          //console.log(">>> pingCluster res " + JSON.stringify(res));
          this.$emit("fetchData");
          swal({
            text: `Cluster Connection Result: ${
              res.data.success ? "Success" : "Failed"
            }`,
            icon: res.data.success ? "success" : "error",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getCluster();
  },
};
</script>

<style>
/* Your existing styles go here */
</style>
