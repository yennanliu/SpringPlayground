<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Cluster</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <Form @submit="addCluster" v-slot="{ meta }">
          <div class="form-group">
            <label>Url</label>
            <Field
              name="url"
              type="text"
              class="form-control"
              v-model="url"
              placeholder="e.g., http://localhost"
              rules="required|url"
            />
            <ErrorMessage name="url" class="invalid-feedback d-block" />
          </div>

          <div class="form-group">
            <label>Port</label>
            <Field
              name="port"
              type="text"
              class="form-control"
              v-model="port"
              placeholder="e.g., 8081"
              rules="required|port"
            />
            <ErrorMessage name="port" class="invalid-feedback d-block" />
          </div>

          <button type="submit" class="btn btn-primary" :disabled="!meta.valid || loading">
            Submit
            <div v-if="loading" class="spinner-border spinner-border-sm" role="status">
              <span class="sr-only">Loading...</span>
            </div>
          </button>
        </Form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate'
import swal from "sweetalert"
import { clusterService } from "@/services"

const router = useRouter()
const url = ref('')
const port = ref('')
const loading = ref(false)

const addCluster = async () => {
  loading.value = true
  try {
    await clusterService.create({
      url: url.value,
      port: port.value,
    })
    router.push({ name: "ListCluster" })
    swal({
      text: "Cluster Added Successfully!",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Failed to add cluster",
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
