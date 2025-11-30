<template>
  <div v-if="show" class="message-actions">
    <button
      v-if="canEdit"
      @click="handleEdit"
      class="action-button"
      title="Edit message"
    >
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
      </svg>
      Edit
    </button>

    <button
      @click="handleCopy"
      class="action-button"
      title="Copy message"
    >
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
        <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
      </svg>
      Copy
    </button>

    <button
      v-if="canDelete"
      @click="handleDelete"
      class="action-button delete"
      title="Delete message"
    >
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="3 6 5 6 21 6"></polyline>
        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
      </svg>
      Delete
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  message: {
    type: Object,
    required: true
  },
  isOwnMessage: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['edit', 'copy', 'delete'])

const canEdit = computed(() => props.isOwnMessage)
const canDelete = computed(() => props.isOwnMessage)

function handleEdit() {
  emit('edit', props.message)
}

function handleCopy() {
  emit('copy', props.message)
}

function handleDelete() {
  emit('delete', props.message)
}
</script>

<style scoped>
.message-actions {
  display: flex;
  gap: 4px;
  padding: 4px;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  position: absolute;
  top: -32px;
  right: 8px;
  z-index: 10;
  animation: fadeIn 0.15s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(4px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.action-button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  background: none;
  border: none;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
  color: #333;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.action-button:hover {
  background-color: #f0f0f0;
}

.action-button.delete {
  color: #f44336;
}

.action-button.delete:hover {
  background-color: #ffebee;
}

.action-button svg {
  flex-shrink: 0;
}
</style>
