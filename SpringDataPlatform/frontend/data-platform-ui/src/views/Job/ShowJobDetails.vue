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
            <td>
              <a :href="getFlinkJobLink(job)" target="_blank">
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

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { jobService } from "@/services"

const route = useRoute()
const job = ref({})
const loading = ref(false)
const error = ref(null)

const getFlinkJobLink = (job) => {
  return jobService.getFlinkJobLink(job)
}

const getJob = async () => {
  loading.value = true
  try {
    job.value = await jobService.getById(route.params.id)
  } catch (err) {
    error.value = "Failed to load job details"
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getJob()
})
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
