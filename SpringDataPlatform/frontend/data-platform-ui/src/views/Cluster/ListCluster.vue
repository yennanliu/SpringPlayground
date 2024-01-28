<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Cluster List</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Url</th>
            <th>Port</th>
            <th>Status</th>
            <th>Detail</th>
            <!-- Add more columns if needed -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="cluster in clusters" :key="cluster.id">
            <td>{{ cluster.id }}</td>
            <td>{{ cluster.url }}</td>
            <td>{{ cluster.port }}</td>
            <td :style="{ color: getStatusColor(cluster.status) }">{{ cluster.status }}</td>
            <td>
              <router-link :to="`/clusters/show/${cluster.id}`">
                Cluster Detail
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
// import JobBox from "../../components/Job/JobBox";
var axios = require("axios");

export default {
  name: "ListCluster",
  data() {
    return {
      clusters: [],
      id: null,
      len: 0,
      msg: null,
    };
  },
  // components: { JobBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      try {
        // http://localhost:9999/cluster/
        const response = await axios.get(`${this.baseURL}/cluster/`);
        this.clusters = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    getStatusColor(status) {
      // Add logic to determine color based on status
      return status === 'connected' ? 'green' : 'red';
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
