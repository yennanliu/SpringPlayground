<template>
  <div class="container page-wrap">

    <!-- Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Jobs</h1>
        <p class="page-subtitle">Flink JAR jobs and SQL jobs</p>
      </div>
      <div style="display:flex;gap:10px">
        <router-link :to="{ name: 'AddSqlJob' }" class="btn btn-ghost">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <ellipse cx="12" cy="5" rx="9" ry="3"/>
            <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
            <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
          </svg>
          SQL Job
        </router-link>
        <router-link :to="{ name: 'AddJob' }" class="btn btn-primary">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          JAR Job
        </router-link>
      </div>
    </div>

    <!-- ── JAR Jobs ──────────────────────────────────────────────────── -->
    <div class="section-label">
      <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
      </svg>
      JAR Jobs
    </div>

    <div class="table-card">
      <!-- loading -->
      <div v-if="loadingJobs" class="state-box">
        <div class="spinner" /><p style="color:var(--color-gray-500);margin-top:12px">Loading JAR jobs…</p>
      </div>
      <!-- error -->
      <div v-else-if="jobsError" class="alert-danger" style="margin:16px">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
        {{ jobsError }}
      </div>
      <!-- empty -->
      <div v-else-if="jobs.length === 0" class="state-box">
        <div class="state-icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <polygon points="5 3 19 12 5 21 5 3"/>
          </svg>
        </div>
        <p class="state-title">No JAR jobs yet</p>
        <p class="state-desc">Upload a JAR and submit your first job</p>
        <router-link :to="{ name: 'AddJob' }" class="btn btn-primary btn-sm">Submit JAR Job</router-link>
      </div>
      <!-- table -->
      <table v-else class="tbl">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Flink Job ID</th>
            <th>State</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in jobs" :key="job.id">
            <td style="font-family:var(--font-mono);color:var(--color-gray-400);font-size:.78rem">{{ job.id }}</td>
            <td>
              <div style="display:flex;align-items:center;gap:10px">
                <div style="width:30px;height:30px;background:var(--color-gray-100);border-radius:6px;display:flex;align-items:center;justify-content:center;color:var(--color-gray-500)">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="5 3 19 12 5 21 5 3"/></svg>
                </div>
                <span style="font-weight:500">{{ job.name || '—' }}</span>
              </div>
            </td>
            <td style="font-family:var(--font-mono);font-size:.78rem;color:var(--color-gray-500)">
              {{ job.jobId ? job.jobId.substring(0, 8) + '…' : '—' }}
            </td>
            <td>
              <span :class="['badge', jobBadge(job.state)]">
                <span class="badge-dot" />
                {{ job.state || 'Unknown' }}
              </span>
            </td>
            <td>
              <router-link :to="`/jobs/show/${job.id}`" class="row-action">
                Details
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18l6-6-6-6"/></svg>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- ── SQL Jobs ──────────────────────────────────────────────────── -->
    <div class="section-label" style="margin-top:32px">
      <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <ellipse cx="12" cy="5" rx="9" ry="3"/>
        <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
        <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
      </svg>
      SQL Jobs
    </div>

    <div class="table-card">
      <!-- loading -->
      <div v-if="loadingSqlJobs" class="state-box">
        <div class="spinner" /><p style="color:var(--color-gray-500);margin-top:12px">Loading SQL jobs…</p>
      </div>
      <!-- error -->
      <div v-else-if="sqlJobsError" class="alert-danger" style="margin:16px">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
        {{ sqlJobsError }}
      </div>
      <!-- empty -->
      <div v-else-if="sqlJobs.length === 0" class="state-box">
        <div class="state-icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <ellipse cx="12" cy="5" rx="9" ry="3"/>
            <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
            <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
          </svg>
        </div>
        <p class="state-title">No SQL jobs yet</p>
        <p class="state-desc">Submit a Flink SQL statement to get started</p>
        <router-link :to="{ name: 'AddSqlJob' }" class="btn btn-primary btn-sm">Submit SQL Job</router-link>
      </div>
      <!-- table -->
      <table v-else class="tbl">
        <thead>
          <tr>
            <th>ID</th>
            <th>Statement</th>
            <th>Session</th>
            <th>Operation</th>
            <th>Status</th>
            <th>Submitted</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="job in sqlJobs" :key="job.id">
            <td style="font-family:var(--font-mono);color:var(--color-gray-400);font-size:.78rem">{{ job.id }}</td>
            <td style="max-width:260px">
              <code class="sql-snippet">{{ truncate(job.statement, 60) }}</code>
            </td>
            <td style="font-family:var(--font-mono);font-size:.72rem;color:var(--color-gray-400)">
              {{ job.sessionHandle ? job.sessionHandle.substring(0, 8) + '…' : '—' }}
            </td>
            <td style="font-family:var(--font-mono);font-size:.72rem;color:var(--color-gray-400)">
              {{ job.operationHandle ? job.operationHandle.substring(0, 8) + '…' : '—' }}
            </td>
            <td>
              <span :class="['badge', sqlStatusBadge(job.status)]">
                <span class="badge-dot" v-if="job.status === 'RUNNING'" />
                {{ job.status || '—' }}
              </span>
            </td>
            <td style="font-size:.78rem;color:var(--color-gray-500)">
              {{ formatDate(job.createdAt) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { jobService } from '@/services'

const jobs         = ref([])
const sqlJobs      = ref([])
const loadingJobs    = ref(false)
const loadingSqlJobs = ref(false)
const jobsError      = ref(null)
const sqlJobsError   = ref(null)

const fetchJobs = async () => {
  loadingJobs.value = true
  try { jobs.value = await jobService.getAll() }
  catch { jobsError.value = 'Failed to load JAR jobs' }
  finally { loadingJobs.value = false }
}

const fetchSqlJobs = async () => {
  loadingSqlJobs.value = true
  try { sqlJobs.value = await jobService.getAllSqlJobs() }
  catch { sqlJobsError.value = 'Failed to load SQL jobs' }
  finally { loadingSqlJobs.value = false }
}

onMounted(() => { fetchJobs(); fetchSqlJobs() })

const jobBadge = (state) => {
  if (!state) return 'badge-neutral'
  const s = state.toLowerCase()
  if (['running', 'finished'].includes(s)) return 'badge-success'
  if (['failed', 'canceled'].includes(s))  return 'badge-danger'
  if (['created', 'scheduled'].includes(s)) return 'badge-info'
  return 'badge-neutral'
}

const sqlStatusBadge = (status) => {
  if (!status) return 'badge-neutral'
  const s = status.toUpperCase()
  if (s === 'RUNNING')    return 'badge-success'
  if (s === 'FAILED')     return 'badge-danger'
  if (s === 'SUBMITTING') return 'badge-warning'
  return 'badge-neutral'
}

const truncate = (str, n) => str && str.length > n ? str.substring(0, n) + '…' : (str || '')

const formatDate = (dt) => {
  if (!dt) return '—'
  try { return new Date(dt).toLocaleString() } catch { return dt }
}
</script>

<style scoped>
.section-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.78rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.07em;
  color: var(--color-gray-500);
  margin-bottom: 10px;
}
.row-action {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-gray-700);
  text-decoration: none;
  transition: color var(--transition);
}
.row-action:hover { color: var(--color-black); }
.row-action svg { transition: transform var(--transition); }
.row-action:hover svg { transform: translateX(2px); }
.sql-snippet {
  font-family: var(--font-mono);
  font-size: 0.78rem;
  background: var(--color-gray-50);
  padding: 3px 7px;
  border-radius: 4px;
  color: var(--color-gray-700);
  word-break: break-all;
}
</style>
