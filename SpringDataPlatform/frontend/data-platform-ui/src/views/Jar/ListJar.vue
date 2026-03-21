<template>
  <div class="container py-5">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">JAR Files</h1>
        <p class="page-subtitle">Manage your uploaded Flink JAR files</p>
      </div>
      <div class="page-actions">
        <router-link :to="{ name: 'AddJar' }" class="btn btn-primary">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 8px;">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
          Upload JAR
        </router-link>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="table-wrapper">
      <div class="loading-state">
        <div class="spinner-border" role="status">
          <span class="sr-only">Loading...</span>
        </div>
        <p class="loading-text">Loading JAR files...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="alert-error">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"/>
        <line x1="12" y1="8" x2="12" y2="12"/>
        <line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      {{ error }}
    </div>

    <!-- Empty State -->
    <div v-else-if="jars.length === 0" class="table-wrapper">
      <div class="empty-state">
        <div class="empty-state-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
            <polyline points="3.27 6.96 12 12.01 20.73 6.96"/>
            <line x1="12" y1="22.08" x2="12" y2="12"/>
          </svg>
        </div>
        <h3 class="empty-state-title">No JAR files found</h3>
        <p class="empty-state-description">Upload your first Flink JAR file to get started</p>
        <router-link :to="{ name: 'AddJar' }" class="btn btn-primary">
          Upload JAR
        </router-link>
      </div>
    </div>

    <!-- Table -->
    <div v-else class="table-wrapper">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>File Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="jar in jars" :key="jar.id">
            <td class="cell-id">{{ jar.id }}</td>
            <td class="cell-name">
              <div class="jar-name-cell">
                <div class="jar-icon">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
                  </svg>
                </div>
                <div class="jar-info">
                  <span class="jar-filename">{{ jar.fileName }}</span>
                  <span class="jar-type">.jar</span>
                </div>
              </div>
            </td>
            <td>
              <router-link :to="`/jars/show/${jar.id}`" class="table-action">
                View Details
                <svg class="action-arrow" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M9 18l6-6-6-6"/>
                </svg>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { jarService } from "@/services"

const jars = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    jars.value = await jarService.getAll()
  } catch (err) {
    error.value = "Failed to load JAR files"
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.py-5 {
  padding-top: 40px;
  padding-bottom: 40px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.page-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #212529;
  margin: 0;
}

.page-subtitle {
  color: #6c757d;
  margin: 4px 0 0;
  font-size: 0.95rem;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  background-color: #000000;
  color: #ffffff;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease;
  border: none;
}

.btn-primary:hover {
  background-color: #333333;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.table-wrapper {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.07);
  overflow: hidden;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.loading-text {
  margin-top: 16px;
  color: #6c757d;
}

.alert-error {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background-color: rgba(220, 53, 69, 0.1);
  border-radius: 8px;
  color: #dc3545;
  font-weight: 500;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-state-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #f8f9fa;
  margin-bottom: 24px;
  color: #adb5bd;
}

.empty-state-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 8px;
}

.empty-state-description {
  color: #6c757d;
  margin: 0 0 24px;
}

.table {
  width: 100%;
  margin: 0;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 16px 20px;
  text-align: left;
  vertical-align: middle;
}

.table thead th {
  background-color: #fafafa;
  font-weight: 600;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #6c757d;
  border-bottom: 2px solid #f0f0f0;
}

.table tbody tr {
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.15s ease;
}

.table tbody tr:hover {
  background-color: rgba(240, 193, 75, 0.05);
}

.table tbody tr:last-child {
  border-bottom: none;
}

.cell-id {
  font-family: 'Courier New', monospace;
  color: #6c757d;
  font-size: 0.85rem;
}

.cell-name {
  font-weight: 500;
}

.jar-name-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.jar-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background-color: rgba(240, 193, 75, 0.1);
  color: #d4a83a;
}

.jar-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.jar-filename {
  font-weight: 500;
  color: #212529;
}

.jar-type {
  font-size: 0.75rem;
  color: #6c757d;
  background-color: #f8f9fa;
  padding: 2px 6px;
  border-radius: 4px;
}

.table-action {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #000000;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.15s ease;
}

.table-action:hover {
  color: #f0c14b;
}

.action-arrow {
  transition: transform 0.2s ease;
}

.table-action:hover .action-arrow {
  transform: translateX(3px);
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .table-wrapper {
    overflow-x: auto;
  }

  .table {
    min-width: 400px;
  }
}
</style>
