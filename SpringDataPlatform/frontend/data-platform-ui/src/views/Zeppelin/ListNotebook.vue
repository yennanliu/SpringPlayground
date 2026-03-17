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
            <!-- Add more columns if needed -->
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
              <a :href="`${getZeppelinNotebookLink(notebook)}`" target="_blank">
                View Notebook
              </a>
            </td>
            <!-- Add more columns if needed -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { zeppelinService } from "@/services";

export default {
  name: "ListNotebook",
  data() {
    return {
      notebooks: [],
      loading: false,
      error: null,
    };
  },
  methods: {
    getZeppelinNotebookLink(notebook) {
      return zeppelinService.getNotebookLink(notebook);
    },

    async fetchData() {
      this.loading = true;
      this.error = null;
      try {
        this.notebooks = await zeppelinService.getAll();
      } catch (error) {
        this.error = "Failed to load notebooks";
      } finally {
        this.loading = false;
      }
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
