<template>
  <div class="form-page">
    <div class="form-card">

      <div class="form-header">
        <router-link :to="{ name: 'ListCluster' }" class="back-link">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7"/>
          </svg>
          All Clusters
        </router-link>
        <div class="form-icon-wrap">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="2" y="2" width="20" height="8" rx="2"/>
            <rect x="2" y="14" width="20" height="8" rx="2"/>
            <line x1="6" y1="6" x2="6.01" y2="6"/>
            <line x1="6" y1="18" x2="6.01" y2="18"/>
          </svg>
        </div>
        <h1>Add Cluster</h1>
        <p>Register a new Flink JobManager endpoint</p>
      </div>

      <Form @submit="addCluster" v-slot="{ meta }" class="form-body">

        <div class="field">
          <label>Cluster URL</label>
          <div class="input-wrap">
            <svg class="input-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="2" y1="12" x2="22" y2="12"/>
              <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
            </svg>
            <Field
              name="url" type="text"
              class="input" v-model="url"
              rules="required|url"
              placeholder="http://localhost"
            />
          </div>
          <ErrorMessage name="url" class="field-error" />
        </div>

        <div class="field">
          <label>Port</label>
          <div class="input-wrap">
            <svg class="input-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="2" y="2" width="20" height="8" rx="2"/>
              <rect x="2" y="14" width="20" height="8" rx="2"/>
            </svg>
            <Field
              name="port" type="text"
              class="input" v-model="port"
              rules="required|port"
              placeholder="8081"
            />
          </div>
          <ErrorMessage name="port" class="field-error" />
        </div>

        <div v-if="errorMsg" class="submit-error">{{ errorMsg }}</div>

        <button type="submit" class="submit-btn" :disabled="!meta.valid || loading">
          <template v-if="loading">
            <div class="spinner-sm" /> Registering…
          </template>
          <template v-else>
            Register Cluster
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </template>
        </button>

      </Form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate'
import swal from 'sweetalert'
import { clusterService } from '@/services'

const router   = useRouter()
const url      = ref('')
const port     = ref('')
const loading  = ref(false)
const errorMsg = ref('')

const addCluster = async () => {
  errorMsg.value = ''
  loading.value  = true
  try {
    await clusterService.create({ url: url.value, port: port.value })
    router.push({ name: 'ListCluster' })
    swal({ text: 'Cluster registered successfully!', icon: 'success', closeOnClickOutside: false })
  } catch {
    errorMsg.value = 'Failed to register cluster. Please check the URL and port.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* styles live in main.scss (.form-page, .form-card, .field, .input, etc.) */
</style>
