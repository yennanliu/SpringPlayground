<template>
  <div class="message-input-container">
    <TypingIndicator :typing-users="currentChannelTypingUsers" />
    <form @submit.prevent="sendMessage" class="message-form">
      <textarea
        ref="inputField"
        v-model="messageText"
        placeholder="Type a message..."
        :disabled="!isConnected"
        @keydown.enter.exact.prevent="sendMessage"
        @keydown.enter.shift.exact="handleShiftEnter"
        class="message-textarea"
        rows="1"
      ></textarea>

      <button
        type="submit"
        :disabled="!canSend"
        class="send-button"
        :title="isConnected ? 'Send message' : 'Not connected'"
      >
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <line x1="22" y1="2" x2="11" y2="13"></line>
          <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
        </svg>
      </button>
    </form>

    <div v-if="characterCount > maxLength - 100" class="character-count">
      {{ characterCount }} / {{ maxLength }}
    </div>

    <div v-if="!isConnected" class="connection-warning">
      <span>Not connected. Please wait...</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { useMessagesStore } from '../stores/messages'
import { useChannelsStore } from '../stores/channels'
import { useUserStore } from '../stores/user'
import { useWebSocketStore } from '../stores/websocket'
import { useTypingStore } from '../stores/typing'
import websocketService from '../services/websocket.service'
import TypingIndicator from './TypingIndicator.vue'

const messageStore = useMessagesStore()
const channelsStore = useChannelsStore()
const userStore = useUserStore()
const wsStore = useWebSocketStore()
const typingStore = useTypingStore()

const messageText = ref('')
const inputField = ref(null)
const maxLength = 2000
const isTyping = ref(false)
const typingTimeout = ref(null)

const isConnected = computed(() => wsStore.isConnected)

const characterCount = computed(() => messageText.value.length)

const canSend = computed(() => {
  return (
    isConnected.value &&
    messageText.value.trim().length > 0 &&
    characterCount.value <= maxLength
  )
})

const currentChannelTypingUsers = computed(() => {
  const channelId = channelsStore.currentChannelId
  const users = typingStore.getTypingUsersForChannel(channelId)
  // Filter out current user
  return users.filter(username => username !== userStore.username)
})

function sendMessage() {
  if (!canSend.value) return

  const content = messageText.value.trim()

  const message = {
    id: Date.now().toString(),
    senderId: userStore.userId,
    senderName: userStore.username,
    content: content,
    timestamp: new Date().toISOString(),
    messageType: 'TEXT'
  }

  const channelId = channelsStore.currentChannelId

  // Send via WebSocket
  const success = websocketService.sendMessage(channelId, message)

  if (success) {
    // Stop typing indicator
    stopTyping()

    // Clear input
    messageText.value = ''

    // Reset textarea height
    if (inputField.value) {
      inputField.value.style.height = 'auto'
    }

    // Focus back on input
    nextTick(() => {
      if (inputField.value) {
        inputField.value.focus()
      }
    })
  } else {
    console.error('Failed to send message')
  }
}

function handleTyping() {
  if (!isConnected.value) return

  // Mark as typing
  if (!isTyping.value && messageText.value.length > 0) {
    isTyping.value = true
    sendTypingIndicator()
  }

  // Clear existing timeout
  if (typingTimeout.value) {
    clearTimeout(typingTimeout.value)
  }

  // Set new timeout to stop typing after 2 seconds of inactivity
  typingTimeout.value = setTimeout(() => {
    stopTyping()
  }, 2000)
}

function sendTypingIndicator() {
  // In real implementation, this would send a typing event via WebSocket
  // For now, we'll add to local store for demo
  const channelId = channelsStore.currentChannelId
  typingStore.addTypingUser(channelId, userStore.username)
}

function stopTyping() {
  if (isTyping.value) {
    isTyping.value = false

    // Remove from typing store
    const channelId = channelsStore.currentChannelId
    typingStore.removeTypingUser(channelId, userStore.username)
  }

  if (typingTimeout.value) {
    clearTimeout(typingTimeout.value)
    typingTimeout.value = null
  }
}

function handleShiftEnter(event) {
  // Allow newline with Shift+Enter
  const start = event.target.selectionStart
  const end = event.target.selectionEnd
  const value = messageText.value

  messageText.value = value.substring(0, start) + '\n' + value.substring(end)

  // Move cursor after newline
  nextTick(() => {
    event.target.selectionStart = event.target.selectionEnd = start + 1
  })
}

// Auto-resize textarea and handle typing
watch(messageText, () => {
  if (inputField.value) {
    inputField.value.style.height = 'auto'
    inputField.value.style.height = inputField.value.scrollHeight + 'px'
  }

  // Handle typing indicator
  if (messageText.value.trim().length > 0) {
    handleTyping()
  } else {
    stopTyping()
  }
})
</script>

<style scoped>
.message-input-container {
  border-top: 2px solid #e0e0e0;
  background-color: white;
  padding: 16px 24px;
}

.message-form {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.message-textarea {
  flex: 1;
  padding: 12px 16px;
  font-size: 15px;
  font-family: inherit;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  resize: none;
  max-height: 150px;
  overflow-y: auto;
  transition: border-color 0.3s;
  line-height: 1.5;
}

.message-textarea:focus {
  outline: none;
  border-color: #667eea;
}

.message-textarea:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
  color: #999;
}

.send-button {
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: transform 0.2s, opacity 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 48px;
  height: 48px;
}

.send-button:hover:not(:disabled) {
  transform: translateY(-2px);
}

.send-button:active:not(:disabled) {
  transform: translateY(0);
}

.send-button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  transform: none;
}

.character-count {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
  text-align: right;
}

.connection-warning {
  margin-top: 8px;
  padding: 8px 12px;
  background-color: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 6px;
  font-size: 13px;
  color: #856404;
  text-align: center;
}

/* Scrollbar for textarea */
.message-textarea::-webkit-scrollbar {
  width: 6px;
}

.message-textarea::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.message-textarea::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.message-textarea::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .message-input-container {
    padding: 12px 16px;
  }

  .message-textarea {
    font-size: 14px;
  }

  .send-button {
    min-width: 44px;
    height: 44px;
  }
}
</style>
