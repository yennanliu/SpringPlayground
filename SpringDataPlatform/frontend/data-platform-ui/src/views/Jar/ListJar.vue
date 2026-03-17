<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Jar List</h1>
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
            <th>Jar Name</th>
            <th>Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="jar in jars" :key="jar.id">
            <td>{{ jar.id }}</td>
            <td>{{ jar.fileName }}</td>
            <td>
              <router-link :to="`/jars/show/${jar.id}`">
                Jar Detail
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
import { jarService } from "@/services"

const jars = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    jars.value = await jarService.getAll()
  } catch (err) {
    error.value = "Failed to load JAR files"
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
