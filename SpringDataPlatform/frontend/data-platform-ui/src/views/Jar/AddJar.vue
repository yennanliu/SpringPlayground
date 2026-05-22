<template>
  <div class="form-page">
    <div class="form-card">

      <div class="form-header">
        <router-link :to="{ name: 'ListJar' }" class="back-link">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7"/>
          </svg>
          All JARs
        </router-link>
        <div class="form-icon-wrap">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
        </div>
        <h1>Upload JAR</h1>
        <p>Upload a compiled Flink application JAR</p>
      </div>

      <form @submit.prevent="addJar" class="form-body">

        <div class="field">
          <label>JAR File</label>
          <div class="file-drop" :class="{ 'drag-over': dragging }"
               @dragover.prevent="dragging = true"
               @dragleave="dragging = false"
               @drop.prevent="onDrop">
            <input type="file" ref="fileInput" accept=".jar"
                   @change="onFileChange" style="position:absolute;inset:0;opacity:0;cursor:pointer;z-index:2" />
            <div v-if="!selectedFile">
              <svg class="file-drop-icon" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="17 8 12 3 7 8"/>
                <line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
              <p class="file-drop-label">Drop your JAR here or click to browse</p>
              <p class="file-drop-hint">Only <code>.jar</code> files are accepted</p>
            </div>
            <div v-else class="file-selected" style="position:relative;z-index:3">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
              </svg>
              <div>
                <strong>{{ selectedFile.name }}</strong>
                <span style="margin-left:8px;font-size:.78rem;color:var(--color-gray-500)">
                  {{ (selectedFile.size / 1024).toFixed(1) }} KB
                </span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="errorMsg" class="submit-error">{{ errorMsg }}</div>

        <button type="submit" class="submit-btn" :disabled="!selectedFile || loading">
          <template v-if="loading">
            <div class="spinner-sm" /> Uploading…
          </template>
          <template v-else>
            Upload JAR
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
              <polyline points="17 8 12 3 7 8"/>
              <line x1="12" y1="3" x2="12" y2="15"/>
            </svg>
          </template>
        </button>

      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import swal from 'sweetalert'
import { jarService } from '@/services'

const router       = useRouter()
const fileInput    = ref(null)
const selectedFile = ref(null)
const loading      = ref(false)
const errorMsg     = ref('')
const dragging     = ref(false)

const onFileChange = (e) => {
  selectedFile.value = e.target.files[0] || null
}
const onDrop = (e) => {
  dragging.value = false
  const file = e.dataTransfer.files[0]
  if (file && file.name.endsWith('.jar')) {
    selectedFile.value = file
  } else {
    errorMsg.value = 'Only .jar files are accepted.'
  }
}

const addJar = async () => {
  if (!selectedFile.value) return
  errorMsg.value = ''
  loading.value  = true
  try {
    await jarService.create(selectedFile.value)
    router.push({ name: 'ListJar' })
    swal({ text: 'JAR uploaded successfully!', icon: 'success', closeOnClickOutside: false })
  } catch {
    errorMsg.value = 'Failed to upload the JAR file. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>/* styles in main.scss */</style>
