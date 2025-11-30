<template>
  <div ref="messageContainer" class="message-list">
    <div v-if="sortedMessages.length === 0" class="empty-state">
      <p>No messages yet. Start the conversation!</p>
    </div>

    <div
      v-for="message in sortedMessages"
      :key="message.id"
      class="message-item"
      :class="{ 'own-message': isOwnMessage(message) }"
      @mouseenter="hoveredMessageId = message.id"
      @mouseleave="hoveredMessageId = null"
    >
      <Avatar
        v-if="!isOwnMessage(message)"
        :username="message.senderName"
        :avatar-url="message.senderAvatar"
        size="small"
        class="message-avatar"
      />
      <div class="message-bubble-container">
        <div
          v-if="editingMessageId === message.id"
          class="edit-container"
        >
          <textarea
            v-model="editedContent"
            @keydown.enter.exact.prevent="saveEdit"
            @keydown.esc="cancelEdit"
            class="edit-textarea"
            rows="3"
            ref="editTextarea"
          ></textarea>
          <div class="edit-actions">
            <button @click="cancelEdit" class="edit-button cancel">Cancel</button>
            <button @click="saveEdit" class="edit-button save">Save</button>
          </div>
        </div>
        <div v-else class="message-bubble">
          <div class="message-header">
            <span class="sender-name">{{ message.senderName }}</span>
            <span class="message-time">
              {{ formatTime(message.timestamp) }}
              <span v-if="message.edited" class="edited-indicator">(edited)</span>
            </span>
          </div>
          <div class="message-content">
            {{ message.content }}
          </div>
        </div>
        <MessageActions
          :show="hoveredMessageId === message.id"
          :message="message"
          :is-own-message="isOwnMessage(message)"
          @edit="startEdit"
          @copy="copyMessage"
          @delete="confirmDelete"
        />
      </div>
      <Avatar
        v-if="isOwnMessage(message)"
        :username="message.senderName"
        :avatar-url="message.senderAvatar"
        size="small"
        class="message-avatar"
      />
    </div>

    <!-- Delete Confirmation Dialog -->
    <ConfirmDialog
      :is-open="showDeleteDialog"
      title="Delete Message"
      message="Are you sure you want to delete this message? This action cannot be undone."
      confirm-text="Delete"
      :is-danger="true"
      @confirm="deleteMessage"
      @cancel="cancelDelete"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { useMessagesStore } from '../stores/messages'
import { useUserStore } from '../stores/user'
import Avatar from './Avatar.vue'
import MessageActions from './MessageActions.vue'
import ConfirmDialog from './ConfirmDialog.vue'

const messageStore = useMessagesStore()
const userStore = useUserStore()

const messageContainer = ref(null)
const hoveredMessageId = ref(null)
const editingMessageId = ref(null)
const editedContent = ref('')
const editTextarea = ref(null)
const showDeleteDialog = ref(false)
const messageToDelete = ref(null)

const emit = defineEmits(['message-copied'])

const sortedMessages = computed(() => messageStore.sortedMessages)

function isOwnMessage(message) {
  return message.senderId === userStore.userId
}

function formatTime(timestamp) {
  if (!timestamp) return ''

  try {
    const date = new Date(timestamp)
    const now = new Date()
    const isToday =
      date.getDate() === now.getDate() &&
      date.getMonth() === now.getMonth() &&
      date.getFullYear() === now.getFullYear()

    if (isToday) {
      // Show time only for today's messages
      return date.toLocaleTimeString('en-US', {
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
      })
    } else {
      // Show date and time for older messages
      return date.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
      })
    }
  } catch (e) {
    console.error('Error formatting time:', e)
    return ''
  }
}

function scrollToBottom() {
  if (messageContainer.value) {
    nextTick(() => {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    })
  }
}

function startEdit(message) {
  editingMessageId.value = message.id
  editedContent.value = message.content
  nextTick(() => {
    editTextarea.value?.focus()
  })
}

function cancelEdit() {
  editingMessageId.value = null
  editedContent.value = ''
}

function saveEdit() {
  if (editedContent.value.trim() && editingMessageId.value) {
    messageStore.updateMessage(editingMessageId.value, {
      content: editedContent.value.trim()
    })
    cancelEdit()
    emit('message-copied', 'Message updated successfully')
  }
}

async function copyMessage(message) {
  try {
    await navigator.clipboard.writeText(message.content)
    emit('message-copied', 'Message copied to clipboard')
  } catch (error) {
    console.error('Failed to copy message:', error)
    emit('message-copied', 'Failed to copy message')
  }
}

function confirmDelete(message) {
  messageToDelete.value = message
  showDeleteDialog.value = true
}

function deleteMessage() {
  if (messageToDelete.value) {
    messageStore.deleteMessage(messageToDelete.value.id)
    showDeleteDialog.value = false
    messageToDelete.value = null
    emit('message-copied', 'Message deleted')
  }
}

function cancelDelete() {
  showDeleteDialog.value = false
  messageToDelete.value = null
}

// Watch for new messages and auto-scroll
watch(
  () => messageStore.messages.length,
  () => {
    scrollToBottom()
  }
)

// Initial scroll to bottom
watch(
  messageContainer,
  (newVal) => {
    if (newVal) {
      scrollToBottom()
    }
  },
  { immediate: true }
)
</script>

<style scoped>
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background-color: #f9f9f9;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #999;
  font-size: 16px;
}

.empty-state p {
  margin: 0;
}

.message-item {
  display: flex;
  gap: 8px;
  max-width: 70%;
  align-self: flex-start;
  animation: slideIn 0.2s ease-out;
  align-items: flex-end;
}

.own-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-bubble-container {
  position: relative;
  flex: 1;
  min-width: 0;
}

.message-bubble {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 12px 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.own-message .message-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.edit-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.edit-textarea {
  width: 100%;
  padding: 8px 12px;
  font-size: 15px;
  font-family: inherit;
  border: 2px solid #667eea;
  border-radius: 6px;
  resize: vertical;
  min-height: 60px;
}

.edit-textarea:focus {
  outline: none;
  border-color: #764ba2;
}

.edit-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.edit-button {
  padding: 6px 16px;
  font-size: 13px;
  font-weight: 600;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-button.cancel {
  background-color: #f5f5f5;
  color: #333;
}

.edit-button.cancel:hover {
  background-color: #e0e0e0;
}

.edit-button.save {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.edit-button.save:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.4);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 4px;
}

.sender-name {
  font-size: 13px;
  font-weight: 600;
  color: #667eea;
}

.own-message .message-bubble .sender-name {
  color: rgba(255, 255, 255, 0.9);
}

.message-time {
  font-size: 11px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}

.own-message .message-bubble .message-time {
  color: rgba(255, 255, 255, 0.7);
}

.edited-indicator {
  font-size: 10px;
  font-style: italic;
  opacity: 0.8;
}

.message-content {
  font-size: 15px;
  line-height: 1.5;
  color: #333;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.own-message .message-bubble .message-content {
  color: white;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Scrollbar styling */
.message-list::-webkit-scrollbar {
  width: 8px;
}

.message-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.message-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 4px;
}

.message-list::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .message-list {
    padding: 16px;
    gap: 12px;
  }

  .message-item {
    max-width: 85%;
  }
}
</style>
