<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Notebook List</h1>
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
            <th>Name</th>
            <th>Interpreter Group</th>
            <th>Added Time</th>
            <th>Updated Time</th>
            <th>Detail</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="notebook in notebooks" :key="notebook.id">
            <td>{{ notebook.id }}</td>
            <td>{{ notebook.zeppelinNoteId }}</td>
            <td>{{ notebook.interpreterGroup }}</td>
            <td>{{ notebook.insertTime }}</td>
            <td>{{ notebook.updateTime }}</td>
            <td>
              <a :href="getZeppelinNotebookLink(notebook)" target="_blank">
                View Notebook
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { zeppelinService } from "@/services"

const notebooks = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    notebooks.value = await zeppelinService.getAll()
  } catch (err) {
    error.value = "Failed to load notebooks"
  } finally {
    loading.value = false
  }
}

const getZeppelinNotebookLink = (notebook) => {
  return zeppelinService.getNotebookLink(notebook)
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
