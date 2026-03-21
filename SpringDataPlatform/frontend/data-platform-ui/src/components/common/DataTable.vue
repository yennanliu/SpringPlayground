<template>
  <div class="data-table-wrapper">
    <div v-if="loading" class="loading-state">
      <LoadingSpinner centered :showText="true" loadingText="Loading data..." />
    </div>

    <AlertMessage v-else-if="error" variant="danger" :message="error" />

    <div v-else-if="!items || items.length === 0" class="empty-state">
      <div class="empty-state-icon">
        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"/>
          <polyline points="13 2 13 9 20 9"/>
        </svg>
      </div>
      <h3 class="empty-state-title">No Data Found</h3>
      <p class="empty-state-description">{{ emptyMessage }}</p>
    </div>

    <div v-else class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th v-for="column in columns" :key="column.key">
              {{ column.label }}
            </th>
            <th v-if="$slots.actions">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in items" :key="getItemKey(item, index)">
            <td v-for="column in columns" :key="column.key">
              <slot :name="`cell-${column.key}`" :item="item" :value="getNestedValue(item, column.key)">
                {{ formatValue(getNestedValue(item, column.key), column) }}
              </slot>
            </td>
            <td v-if="$slots.actions">
              <slot name="actions" :item="item" :index="index"></slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import LoadingSpinner from "./LoadingSpinner.vue"
import AlertMessage from "./AlertMessage.vue"

const props = defineProps({
  columns: {
    type: Array,
    required: true,
  },
  items: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  error: {
    type: String,
    default: null,
  },
  itemKey: {
    type: String,
    default: "id",
  },
  emptyMessage: {
    type: String,
    default: "No data available",
  },
})

const getItemKey = (item, index) => {
  return item[props.itemKey] || index
}

const getNestedValue = (obj, path) => {
  return path.split(".").reduce((acc, part) => acc && acc[part], obj)
}

const formatValue = (value, column) => {
  if (value === null || value === undefined) {
    return "-"
  }
  if (column.formatter && typeof column.formatter === "function") {
    return column.formatter(value)
  }
  return value
}
</script>

<style scoped>
.data-table-wrapper {
  width: 100%;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.07);
  overflow: hidden;
}

.loading-state {
  padding: 60px 20px;
}

.table-container {
  overflow-x: auto;
}

.table {
  width: 100%;
  margin-bottom: 0;
  color: #212529;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 14px 20px;
  vertical-align: middle;
  border-top: 1px solid #f0f0f0;
}

.table thead th {
  vertical-align: bottom;
  border-bottom: 2px solid #f0f0f0;
  border-top: none;
  font-weight: 600;
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #6c757d;
  background-color: #fafafa;
  white-space: nowrap;
}

.table tbody tr {
  transition: background-color 0.15s ease;
}

.table tbody tr:hover {
  background-color: rgba(240, 193, 75, 0.05);
}

.table tbody tr:last-child td {
  border-bottom: none;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-state-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #f8f9fa;
  margin-bottom: 20px;
  color: #adb5bd;
}

.empty-state-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 8px;
}

.empty-state-description {
  color: #6c757d;
  margin: 0;
  max-width: 300px;
  margin-left: auto;
  margin-right: auto;
}
</style>
