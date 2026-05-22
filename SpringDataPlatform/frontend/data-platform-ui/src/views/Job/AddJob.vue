<template>
  <div class="form-page">
    <div class="form-card">

      <div class="form-header">
        <router-link :to="{ name: 'ListJob' }" class="back-link">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7"/>
          </svg>
          All Jobs
        </router-link>
        <div class="form-icon-wrap">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polygon points="5 3 19 12 5 21 5 3"/>
          </svg>
        </div>
        <h1>Submit JAR Job</h1>
        <p>Launch a Flink job from an uploaded JAR</p>
      </div>

      <form @submit.prevent="addJob" class="form-body">

        <div class="field">
          <label>Select JAR</label>
          <div v-if="loadingJars" style="display:flex;align-items:center;gap:8px;color:var(--color-gray-500);font-size:.875rem;">
            <div class="spinner-sm" style="border-color:var(--color-gray-300);border-top-color:var(--color-gray-600);" />
            Loading JARs…
          </div>
          <div v-else-if="jars.length === 0" style="padding:14px;background:var(--color-gray-50);border:1.5px solid var(--color-gray-200);border-radius:var(--radius-md);font-size:.875rem;color:var(--color-gray-500);">
            No JARs uploaded yet.
            <router-link :to="{ name: 'AddJar' }" style="color:var(--color-black);font-weight:600;">Upload one first →</router-link>
          </div>
          <select v-else class="input-select" v-model="savedJarId" required>
            <option value="" disabled>Choose a JAR file…</option>
            <option v-for="jar in jars" :key="jar.id" :value="jar.id">
              #{{ jar.id }} — {{ jar.savedJarName || jar.fileName }}
            </option>
          </select>
          <p class="field-hint">Only JARs uploaded to this platform are listed</p>
        </div>

        <div v-if="errorMsg" class="submit-error">{{ errorMsg }}</div>

        <button type="submit" class="submit-btn" :disabled="!savedJarId || loading">
          <template v-if="loading">
            <div class="spinner-sm" /> Submitting…
          </template>
          <template v-else>
            Submit Job
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polygon points="5 3 19 12 5 21 5 3"/>
            </svg>
          </template>
        </button>

      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import swal from 'sweetalert'
import { jarService, jobService } from '@/services'

const router     = useRouter()
const savedJarId = ref('')
const jars       = ref([])
const loading    = ref(false)
const loadingJars = ref(true)
const errorMsg   = ref('')

onMounted(async () => {
  try {
    jars.value = await jarService.getAll()
  } catch {
    errorMsg.value = 'Could not load JAR list.'
  } finally {
    loadingJars.value = false
  }
})

const addJob = async () => {
  errorMsg.value = ''
  loading.value  = true
  try {
    await jobService.create({ jarId: savedJarId.value })
    router.push({ name: 'ListJob' })
    swal({ text: 'Job submitted successfully!', icon: 'success', closeOnClickOutside: false })
  } catch {
    errorMsg.value = 'Failed to submit job. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>/* styles in main.scss */</style>
