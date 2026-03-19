<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Jar</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addJar">
          <div class="form-group">
            <label>Jar File</label>
            <input type="file" ref="fileInput" class="form-control" required />
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
import { jarService } from "@/services"

const router = useRouter()
const fileInput = ref(null)
const loading = ref(false)

const addJar = async () => {
  const file = fileInput.value?.files[0]

  if (!file) {
    swal({
      text: "Please select a JAR file",
      icon: "warning",
      closeOnClickOutside: false,
    })
    return
  }

  loading.value = true
  try {
    await jarService.create(file)
    router.push({ name: "ListJar" })
    swal({
      text: "Jar Added Successfully!",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Failed to upload JAR file",
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
</style>
