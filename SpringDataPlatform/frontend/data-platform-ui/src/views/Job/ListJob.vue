<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Job List</h1>
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
            <th>Job Name</th>
            <th>State</th>
            <th>Detail</th>
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
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { jobService } from "@/services"

const jobs = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    jobs.value = await jobService.getAll()
  } catch (err) {
    error.value = "Failed to load jobs"
  } finally {
    loading.value = false
  }
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
