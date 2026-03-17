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
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { clusterService } from "@/services"

const clusters = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    clusters.value = await clusterService.getAll()
  } catch (err) {
    error.value = "Failed to load clusters"
  } finally {
    loading.value = false
  }
}

const getStatusColor = (status) => {
  return status === 'connected' ? 'green' : 'red'
}

onMounted(() => {
  fetchData()
})
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
