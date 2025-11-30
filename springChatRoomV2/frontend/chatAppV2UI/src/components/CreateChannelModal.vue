<template>
  <Teleport to="body">
    <div v-if="isOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>Create New Channel</h2>
          <button class="close-button" @click="closeModal">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>

        <form @submit.prevent="handleSubmit" class="modal-form">
          <div class="form-group">
            <label for="channelName">Channel Name</label>
            <input
              id="channelName"
              v-model="channelName"
              type="text"
              placeholder="e.g., general, random, team-chat"
              required
              :disabled="isCreating"
              class="form-input"
              maxlength="50"
            />
            <span class="help-text">
              Lowercase, no spaces (use hyphens instead)
            </span>
            <span v-if="errors.channelName" class="error-message">
              {{ errors.channelName }}
            </span>
          </div>

          <div class="form-group">
            <label for="channelDescription">Description (optional)</label>
            <textarea
              id="channelDescription"
              v-model="channelDescription"
              placeholder="What's this channel about?"
              :disabled="isCreating"
              class="form-textarea"
              rows="3"
              maxlength="200"
            ></textarea>
          </div>

          <div v-if="errorMessage" class="error-banner">
            {{ errorMessage }}
          </div>

          <div class="modal-actions">
            <button
              type="button"
              @click="closeModal"
              :disabled="isCreating"
              class="button button-secondary"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="isCreating || !isFormValid"
              class="button button-primary"
            >
              {{ isCreating ? 'Creating...' : 'Create Channel' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useChannelsStore } from '../stores/channels'
import { useUserStore } from '../stores/user'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'created'])

const channelsStore = useChannelsStore()
const userStore = useUserStore()

const channelName = ref('')
const channelDescription = ref('')
const isCreating = ref(false)
const errorMessage = ref('')
const errors = ref({
  channelName: ''
})

const isFormValid = computed(() => {
  return channelName.value.trim().length >= 3 && !errors.value.channelName
})

// Validate channel name
watch(channelName, (newValue) => {
  errors.value.channelName = ''

  if (!newValue.trim()) {
    return
  }

  if (newValue.length < 3) {
    errors.value.channelName = 'Channel name must be at least 3 characters'
    return
  }

  // Check for valid characters (lowercase, numbers, hyphens)
  const validPattern = /^[a-z0-9-]+$/
  if (!validPattern.test(newValue)) {
    errors.value.channelName = 'Only lowercase letters, numbers, and hyphens allowed'
    return
  }
})

async function handleSubmit() {
  if (!isFormValid.value) {
    return
  }

  isCreating.value = true
  errorMessage.value = ''

  try {
    const channel = await channelsStore.createGroupChannel(
      channelName.value.trim(),
      [userStore.userId] // Add current user as member
    )

    // Switch to new channel
    channelsStore.setCurrentChannel(channel.id)

    // Emit created event
    emit('created', channel)

    // Close modal
    closeModal()

    // Reset form
    resetForm()
  } catch (error) {
    console.error('Failed to create channel:', error)
    errorMessage.value = 'Failed to create channel. Please try again.'
  } finally {
    isCreating.value = false
  }
}

function closeModal() {
  emit('close')
  resetForm()
}

function resetForm() {
  channelName.value = ''
  channelDescription.value = ''
  errorMessage.value = ''
  errors.value = { channelName: '' }
}

// Reset form when modal opens
watch(() => props.isOpen, (isOpen) => {
  if (isOpen) {
    resetForm()
  }
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideIn 0.2s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 2px solid #e0e0e0;
}

.modal-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #333;
}

.close-button {
  padding: 8px;
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  border-radius: 6px;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button:hover {
  background-color: #f5f5f5;
  color: #333;
}

.modal-form {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.form-input,
.form-textarea {
  padding: 12px 16px;
  font-size: 15px;
  font-family: inherit;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  transition: border-color 0.3s;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #667eea;
}

.form-input:disabled,
.form-textarea:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.help-text {
  font-size: 12px;
  color: #666;
}

.error-message {
  font-size: 12px;
  color: #f44336;
  font-weight: 500;
}

.error-banner {
  padding: 12px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 6px;
  font-size: 14px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding-top: 8px;
}

.button {
  padding: 12px 24px;
  font-size: 15px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.button-secondary {
  background-color: #f5f5f5;
  color: #333;
}

.button-secondary:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.button-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.button-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.button-primary:active:not(:disabled) {
  transform: translateY(0);
}

/* Scrollbar for modal content */
.modal-content::-webkit-scrollbar {
  width: 8px;
}

.modal-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.modal-content::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 4px;
}

.modal-content::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .modal-header {
    padding: 20px;
  }

  .modal-header h2 {
    font-size: 20px;
  }

  .modal-form {
    padding: 20px;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .button {
    width: 100%;
  }
}
</style>
