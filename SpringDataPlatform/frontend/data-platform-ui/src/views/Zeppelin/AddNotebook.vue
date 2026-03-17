<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Notebook</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addNoteBook">
          <div class="form-group">
            <label>Notebook Name</label>
            <input
              type="text"
              class="form-control"
              v-model="notePath"
              required
            />
          </div>

          <div class="form-group">
            <label>Interpreter</label>
            <select class="form-control" v-model="interpreterGroup" required>
              <option
                v-for="schema of schemas"
                :key="schema.id"
                :value="schema.columnName"
              >
                {{ schema.columnName.toUpperCase() }}
              </option>
            </select>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import swal from "sweetalert"
import { zeppelinService } from "@/services"

const router = useRouter()
const notePath = ref('')
const schemas = ref([])
const interpreterGroup = ref('')
const loading = ref(false)

const addNoteBook = async () => {
  if (!notePath.value || !interpreterGroup.value) {
    swal({
      text: "Please enter notebook name and select interpreter",
      icon: "warning",
      closeOnClickOutside: false,
    })
    return
  }

  loading.value = true
  try {
    await zeppelinService.create({
      notePath: notePath.value,
      interpreterGroup: interpreterGroup.value,
    })
    router.push({ name: "ListNotebook" })
    swal({
      text: "Notebook Added Successfully!",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Failed to create notebook",
      icon: "error",
      closeOnClickOutside: false,
    })
  } finally {
    loading.value = false
  }
}

const getSchemas = async () => {
  try {
    schemas.value = await zeppelinService.getInterpreters()
  } catch (error) {
    swal({
      text: "Failed to load interpreters",
      icon: "error",
      closeOnClickOutside: false,
    })
  }
}

onMounted(() => {
  getSchemas()
})
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
