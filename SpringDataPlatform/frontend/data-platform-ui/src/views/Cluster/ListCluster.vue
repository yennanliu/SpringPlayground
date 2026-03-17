<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Cluster List</h1>
        <h5 v-if="error" class="text-danger">{{ error }}</h5>
        <div v-if="loading" class="spinner-border" role="status">
          <span class="sr-only">Loading...</span>
        </div>
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
import { clusterService } from "@/services";

export default {
  name: "ListCluster",
  data() {
    return {
      clusters: [],
      loading: false,
      error: null,
    };
  },
  methods: {
    async fetchData() {
      this.loading = true;
      this.error = null;
      try {
        this.clusters = await clusterService.getAll();
      } catch (error) {
        this.error = "Failed to load clusters";
      } finally {
        this.loading = false;
      }
    },
    getStatusColor(status) {
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
