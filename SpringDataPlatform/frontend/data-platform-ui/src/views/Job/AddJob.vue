<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Submit a new Flink Job</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addJob">
          <div class="form-group">
            <label>Jar ID</label>
            <select class="form-control" v-model="savedJarId" required>
              <option v-for="jar in jars" :key="jar.id" :value="jar.id">
                Name : {{ jar.id + " " + jar.savedJarName }}
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
import { jarService, jobService } from "@/services"

const router = useRouter()
const savedJarId = ref(null)
const jars = ref([])
const loading = ref(false)

const getJars = async () => {
  try {
    jars.value = await jarService.getAll()
  } catch (error) {
    swal({
      text: "Failed to load JAR files",
      icon: "error",
      closeOnClickOutside: false,
    })
  }
}

const addJob = async () => {
  if (!savedJarId.value) {
    swal({
      text: "Please select a JAR file",
      icon: "warning",
      closeOnClickOutside: false,
    })
    return
  }

  loading.value = true
  try {
    await jobService.create({ jarId: savedJarId.value })
    router.push({ name: "ListJob" })
    swal({
      text: "Job Added Successfully!",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Failed to submit job",
      icon: "error",
      closeOnClickOutside: false,
    })
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getJars()
})
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
