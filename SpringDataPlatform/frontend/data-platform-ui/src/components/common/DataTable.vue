<template>
  <div class="data-table-wrapper">
    <div v-if="loading" class="text-center py-4">
      <LoadingSpinner centered :showText="true" loadingText="Loading data..." />
    </div>

    <AlertMessage v-else-if="error" variant="danger" :message="error" />

    <div v-else-if="!items || items.length === 0" class="text-center py-4">
      <p class="text-muted">{{ emptyMessage }}</p>
    </div>

    <table v-else class="table">
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
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  color: #212529;
}

.table th,
.table td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}

.table thead th {
  vertical-align: bottom;
  border-bottom: 2px solid #dee2e6;
  font-weight: 600;
}
</style>
