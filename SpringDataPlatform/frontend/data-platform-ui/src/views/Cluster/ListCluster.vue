<template>
  <div class="container py-5">
    <!-- Page Header -->
    <div class="page-header">
      <div>
        <h1 class="page-title">Clusters</h1>
        <p class="page-subtitle">Manage your computing clusters</p>
      </div>
      <div class="page-actions">
        <router-link :to="{ name: 'AddCluster' }" class="btn btn-primary">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 8px;">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          Add Cluster
        </router-link>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="table-wrapper">
      <div class="loading-state">
        <div class="spinner-border" role="status">
          <span class="sr-only">Loading...</span>
        </div>
        <p class="loading-text">Loading clusters...</p>
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
    <div v-else-if="clusters.length === 0" class="table-wrapper">
      <div class="empty-state">
        <div class="empty-state-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <rect x="2" y="2" width="20" height="8" rx="2" ry="2"/>
            <rect x="2" y="14" width="20" height="8" rx="2" ry="2"/>
            <line x1="6" y1="6" x2="6.01" y2="6"/>
            <line x1="6" y1="18" x2="6.01" y2="18"/>
          </svg>
        </div>
        <h3 class="empty-state-title">No clusters found</h3>
        <p class="empty-state-description">Get started by adding your first cluster</p>
        <router-link :to="{ name: 'AddCluster' }" class="btn btn-primary">
          Add Cluster
        </router-link>
      </div>
    </div>

    <!-- Table -->
    <div v-else class="table-wrapper">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>URL</th>
            <th>Port</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cluster in clusters" :key="cluster.id">
            <td class="cell-id">{{ cluster.id }}</td>
            <td class="cell-url">{{ cluster.url }}</td>
            <td>{{ cluster.port }}</td>
            <td>
              <span :class="['badge', getStatusBadgeClass(cluster.status)]">
                <span class="status-dot"></span>
                {{ cluster.status || 'Unknown' }}
              </span>
            </td>
            <td>
              <router-link :to="`/clusters/show/${cluster.id}`" class="table-action">
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
import { clusterService } from "@/services"

const clusters = ref([])
const loading = ref(false)
const error = ref(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  try {
    clusters.value = await clusterService.getAll()
  } catch (err) {
    error.value = "Failed to load clusters"
  } finally {
    loading.value = false
  }
}

const getStatusBadgeClass = (status) => {
  if (!status) return 'badge-neutral'
  return status === 'connected' ? 'badge-success' : 'badge-danger'
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
}

.cell-url {
  font-weight: 500;
}

.badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
  text-transform: capitalize;
}

.badge-success {
  background-color: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.badge-danger {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.badge-neutral {
  background-color: #f8f9fa;
  color: #6c757d;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: currentColor;
}

.badge-success .status-dot {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.2);
  }
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
    min-width: 600px;
  }
}
</style>
