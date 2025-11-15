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
    >
      <div class="message-header">
        <span class="sender-name">{{ message.senderName }}</span>
        <span class="message-time">{{ formatTime(message.timestamp) }}</span>
      </div>
      <div class="message-content">
        {{ message.content }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { useMessagesStore } from '../stores/messages'
import { useUserStore } from '../stores/user'

const messageStore = useMessagesStore()
const userStore = useUserStore()

const messageContainer = ref(null)

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
  flex-direction: column;
  gap: 4px;
  padding: 12px 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  max-width: 70%;
  align-self: flex-start;
  animation: slideIn 0.2s ease-out;
}

.own-message {
  align-self: flex-end;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
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

.own-message .sender-name {
  color: rgba(255, 255, 255, 0.9);
}

.message-time {
  font-size: 11px;
  color: #999;
}

.own-message .message-time {
  color: rgba(255, 255, 255, 0.7);
}

.message-content {
  font-size: 15px;
  line-height: 1.5;
  color: #333;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.own-message .message-content {
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
