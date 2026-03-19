<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Submit new SQL Job</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addSQLJob">
          <div class="form-group">
            <label>SQL command</label>
            <textarea
              class="form-control"
              v-model="statement"
              rows="5"
              required
            ></textarea>
          </div>
          <button type="submit" class="btn btn-primary" :disabled="loading">
            Submit
            <div v-if="loading" class="spinner-border spinner-border-sm" role="status">
              <span class="sr-only">Loading...</span>
            </div>
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import swal from "sweetalert"
import { jobService } from "@/services"

const router = useRouter()
const statement = ref('')
const loading = ref(false)

const addSQLJob = async () => {
  if (!statement.value || !statement.value.trim()) {
    swal({
      text: "Please enter a SQL statement",
      icon: "warning",
      closeOnClickOutside: false,
    })
    return
  }

  loading.value = true
  try {
    await jobService.createSqlJob(statement.value)
    router.push({ name: "ListJob" })
    swal({
      text: "SQL Job Added Successfully!",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Failed to submit SQL job",
      icon: "error",
      closeOnClickOutside: false,
    })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

/* Additional styling for the textarea */
textarea {
  font-family: "Courier New", monospace;
}
</style>
