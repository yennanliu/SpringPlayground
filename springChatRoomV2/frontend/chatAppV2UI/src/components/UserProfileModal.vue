<template>
  <Teleport to="body">
    <div v-if="isOpen" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ isOwnProfile ? 'Your Profile' : `${user.username}'s Profile` }}</h2>
          <button class="close-button" @click="closeModal">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <!-- Avatar Section -->
          <div class="avatar-section">
            <Avatar
              :username="user.username"
              :avatar-url="user.avatarUrl"
              :show-online="!isOwnProfile"
              :is-online="user.isOnline"
              size="xlarge"
            />
            <input
              v-if="isOwnProfile && isEditing"
              ref="fileInput"
              type="file"
              accept="image/*"
              @change="handleAvatarChange"
              class="file-input"
            />
            <button
              v-if="isOwnProfile && isEditing"
              @click="$refs.fileInput.click()"
              class="upload-button"
            >
              Change Avatar
            </button>
          </div>

          <!-- Profile Information -->
          <div class="profile-info">
            <div class="info-group">
              <label>Username</label>
              <input
                v-if="isOwnProfile && isEditing"
                v-model="editedProfile.username"
                type="text"
                class="form-input"
                :disabled="isSaving"
              />
              <p v-else class="info-value">{{ user.username }}</p>
            </div>

            <div class="info-group">
              <label>Display Name</label>
              <input
                v-if="isOwnProfile && isEditing"
                v-model="editedProfile.displayName"
                type="text"
                class="form-input"
                placeholder="Your display name"
                :disabled="isSaving"
              />
              <p v-else class="info-value">{{ user.displayName || 'Not set' }}</p>
            </div>

            <div class="info-group">
              <label>Email</label>
              <input
                v-if="isOwnProfile && isEditing"
                v-model="editedProfile.email"
                type="email"
                class="form-input"
                :disabled="isSaving"
              />
              <p v-else class="info-value">{{ user.email }}</p>
            </div>

            <div class="info-group">
              <label>Status Message</label>
              <textarea
                v-if="isOwnProfile && isEditing"
                v-model="editedProfile.statusMessage"
                class="form-textarea"
                placeholder="What's on your mind?"
                rows="3"
                maxlength="200"
                :disabled="isSaving"
              ></textarea>
              <p v-else class="info-value">{{ user.statusMessage || 'No status set' }}</p>
            </div>

            <div v-if="!isOwnProfile" class="info-group">
              <label>Status</label>
              <p class="info-value">
                <span :class="['status-badge', user.isOnline ? 'online' : 'offline']">
                  {{ user.isOnline ? 'Online' : 'Offline' }}
                </span>
              </p>
            </div>
          </div>

          <!-- Error Message -->
          <div v-if="errorMessage" class="error-banner">
            {{ errorMessage }}
          </div>
        </div>

        <div class="modal-footer">
          <button
            v-if="!isOwnProfile"
            @click="startDirectMessage"
            class="button button-primary"
          >
            Send Message
          </button>

          <template v-if="isOwnProfile">
            <button
              v-if="!isEditing"
              @click="startEditing"
              class="button button-primary"
            >
              Edit Profile
            </button>
            <template v-else>
              <button
                @click="cancelEditing"
                :disabled="isSaving"
                class="button button-secondary"
              >
                Cancel
              </button>
              <button
                @click="saveProfile"
                :disabled="isSaving"
                class="button button-primary"
              >
                {{ isSaving ? 'Saving...' : 'Save Changes' }}
              </button>
            </template>
          </template>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '../stores/user'
import { useChannelsStore } from '../stores/channels'
import Avatar from './Avatar.vue'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  user: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'updated'])

const userStore = useUserStore()
const channelsStore = useChannelsStore()

const isEditing = ref(false)
const isSaving = ref(false)
const errorMessage = ref('')
const editedProfile = ref({})
const fileInput = ref(null)
const selectedAvatar = ref(null)

const isOwnProfile = computed(() => {
  return props.user.id === userStore.userId
})

function startEditing() {
  isEditing.value = true
  editedProfile.value = {
    username: props.user.username,
    displayName: props.user.displayName || '',
    email: props.user.email,
    statusMessage: props.user.statusMessage || ''
  }
}

function cancelEditing() {
  isEditing.value = false
  editedProfile.value = {}
  selectedAvatar.value = null
  errorMessage.value = ''
}

async function saveProfile() {
  isSaving.value = true
  errorMessage.value = ''

  try {
    // In real implementation, this would call an API
    // For now, update the user store
    await new Promise(resolve => setTimeout(resolve, 500)) // Simulate API call

    userStore.updateProfile({
      ...editedProfile.value,
      avatarUrl: selectedAvatar.value || props.user.avatarUrl
    })

    emit('updated', editedProfile.value)
    isEditing.value = false
  } catch (error) {
    console.error('Failed to save profile:', error)
    errorMessage.value = 'Failed to save profile. Please try again.'
  } finally {
    isSaving.value = false
  }
}

function handleAvatarChange(event) {
  const file = event.target.files[0]
  if (file) {
    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      errorMessage.value = 'Image must be less than 5MB'
      return
    }

    // Validate file type
    if (!file.type.startsWith('image/')) {
      errorMessage.value = 'File must be an image'
      return
    }

    // Create preview URL
    const reader = new FileReader()
    reader.onload = (e) => {
      selectedAvatar.value = e.target.result
    }
    reader.readAsDataURL(file)

    errorMessage.value = ''
  }
}

async function startDirectMessage() {
  try {
    const channel = await channelsStore.createDirectChannel(
      userStore.userId,
      props.user.id
    )

    channelsStore.setCurrentChannel(channel.id)
    closeModal()
  } catch (error) {
    console.error('Failed to start direct message:', error)
    errorMessage.value = 'Failed to start conversation. Please try again.'
  }
}

function closeModal() {
  emit('close')
  cancelEditing()
}

// Reset state when modal opens/closes
watch(() => props.isOpen, (isOpen) => {
  if (!isOpen) {
    cancelEditing()
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

.modal-body {
  padding: 24px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-bottom: 32px;
}

.file-input {
  display: none;
}

.upload-button {
  padding: 8px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s;
}

.upload-button:hover {
  transform: translateY(-2px);
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-group label {
  font-size: 13px;
  font-weight: 600;
  color: #666;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  margin: 0;
  font-size: 15px;
  color: #333;
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 6px;
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

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 600;
}

.status-badge.online {
  background-color: #d4f4dd;
  color: #2d7a3e;
}

.status-badge.offline {
  background-color: #f0f0f0;
  color: #757575;
}

.error-banner {
  margin-top: 16px;
  padding: 12px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 6px;
  font-size: 14px;
}

.modal-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding: 24px;
  border-top: 2px solid #e0e0e0;
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

/* Scrollbar */
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

  .modal-body {
    padding: 20px;
  }

  .modal-footer {
    flex-direction: column-reverse;
    padding: 20px;
  }

  .button {
    width: 100%;
  }
}
</style>
