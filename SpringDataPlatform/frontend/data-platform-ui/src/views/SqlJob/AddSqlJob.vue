<template>
  <div class="form-page">
    <div class="form-card" style="max-width:640px">

      <div class="form-header">
        <router-link :to="{ name: 'ListJob' }" class="back-link">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M19 12H5M12 5l-7 7 7 7"/>
          </svg>
          All Jobs
        </router-link>
        <div class="form-icon-wrap">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <ellipse cx="12" cy="5" rx="9" ry="3"/>
            <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
            <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
          </svg>
        </div>
        <h1>Submit SQL Job</h1>
        <p>Run Flink SQL via the SQL Gateway</p>
      </div>

      <form @submit.prevent="addSQLJob" class="form-body">

        <div class="field">
          <label>SQL Statement</label>
          <div class="input-wrap">
            <svg class="textarea-icon" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="4 17 10 11 4 5"/>
              <line x1="12" y1="19" x2="20" y2="19"/>
            </svg>
            <textarea
              class="input-textarea"
              v-model="statement"
              rows="8"
              placeholder="SELECT * FROM my_table&#10;WHERE event_time > NOW() - INTERVAL '1' HOUR"
              required
            />
          </div>
          <p class="field-hint">Flink SQL syntax. The statement is sent to the SQL Gateway session API.</p>
        </div>

        <div v-if="errorMsg" class="submit-error">
          <div>{{ errorMsg }}</div>
          <div style="margin-top:8px;font-size:.78rem;opacity:.8">
            Start the Flink stack with:
            <code style="background:rgba(0,0,0,.08);padding:2px 6px;border-radius:4px;font-family:var(--font-mono)">
              docker compose --profile flink up --build
            </code>
          </div>
        </div>

        <button type="submit" class="submit-btn" :disabled="!statement.trim() || loading">
          <template v-if="loading">
            <div class="spinner-sm" /> Submitting…
          </template>
          <template v-else>
            Run SQL Job
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import swal from 'sweetalert'
import { jobService } from '@/services'

const router    = useRouter()
const statement = ref('')
const loading   = ref(false)
const errorMsg  = ref('')

const addSQLJob = async () => {
  errorMsg.value = ''
  loading.value  = true
  try {
    await jobService.createSqlJob(statement.value)
    router.push({ name: 'ListJob' })
    swal({ text: 'SQL job submitted successfully!', icon: 'success', closeOnClickOutside: false })
  } catch {
    errorMsg.value = 'Failed to submit SQL job. Check that the SQL Gateway is reachable.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>/* styles in main.scss */</style>
