<template>
  <div class="form-page">
    <div class="form-card" style="max-width:660px">

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

        <!-- Info banner -->
        <div class="info-banner">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="flex-shrink:0">
            <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
          <div>
            <strong>SQL jobs are tracked in the platform DB</strong> (visible in the Jobs list below).<br>
            They only appear in the <a href="http://localhost:8081" target="_blank" class="info-link">Flink UI</a>
            when a streaming DML statement creates a long-running Flink job — see the example below.
          </div>
        </div>

        <div class="field">
          <label>SQL Statement</label>
          <div class="input-wrap">
            <svg class="textarea-icon" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="4 17 10 11 4 5"/><line x1="12" y1="19" x2="20" y2="19"/>
            </svg>
            <textarea
              class="input-textarea"
              v-model="statement"
              rows="8"
              :placeholder="placeholder"
              required
            />
          </div>

          <!-- Quick-fill examples -->
          <div class="examples">
            <span class="examples-label">Examples:</span>
            <button type="button" class="example-chip" @click="fill('SELECT 1')">SELECT 1</button>
            <button type="button" class="example-chip" @click="fill(streamingExample)">Streaming job →</button>
          </div>

          <p class="field-hint">
            Simple <code>SELECT</code> expressions execute immediately and are tracked in the Jobs list.<br>
            <code>INSERT INTO … SELECT …</code> with connector tables creates a visible Flink streaming job.
          </p>
        </div>

        <div v-if="errorMsg" class="submit-error">
          <div>{{ errorMsg }}</div>
          <div style="margin-top:8px;font-size:.78rem;opacity:.8">
            Make sure the Flink stack is running:
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

const placeholder = `-- Simple query (tracked in Jobs list, not in Flink UI):
SELECT 1

-- Streaming job (appears in Flink UI at localhost:8081):
-- Requires connector tables — click "Streaming job →" above for an example`

const streamingExample =
`CREATE TABLE IF NOT EXISTS datagen_src (
  id   BIGINT,
  name STRING
) WITH (
  'connector'      = 'datagen',
  'rows-per-second'= '1'
);

CREATE TABLE IF NOT EXISTS print_sink (
  id   BIGINT,
  name STRING
) WITH (
  'connector' = 'print'
);

INSERT INTO print_sink SELECT id, name FROM datagen_src`

const fill = (text) => { statement.value = text }

const addSQLJob = async () => {
  errorMsg.value = ''
  loading.value  = true
  try {
    await jobService.createSqlJob(statement.value)
    router.push({ name: 'ListJob' })
    swal({ text: 'SQL job submitted successfully!', icon: 'success', closeOnClickOutside: false })
  } catch {
    errorMsg.value = 'Failed to submit SQL job.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.info-banner {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 13px 16px;
  background: var(--color-info-bg);
  border: 1px solid rgba(59,130,246,.2);
  border-radius: var(--radius-md);
  font-size: 0.83rem;
  color: var(--color-gray-700);
  line-height: 1.6;
}
.info-link { color: var(--color-info); font-weight: 600; }

.examples {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
  margin-top: 8px;
}
.examples-label {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--color-gray-500);
  text-transform: uppercase;
  letter-spacing: .04em;
}
.example-chip {
  padding: 3px 10px;
  background: var(--color-gray-100);
  border: 1px solid var(--color-gray-200);
  border-radius: var(--radius-full);
  font-family: var(--font-mono);
  font-size: 0.75rem;
  color: var(--color-gray-700);
  cursor: pointer;
  transition: all var(--transition);
}
.example-chip:hover {
  background: var(--color-black);
  color: var(--color-white);
  border-color: var(--color-black);
}
</style>
