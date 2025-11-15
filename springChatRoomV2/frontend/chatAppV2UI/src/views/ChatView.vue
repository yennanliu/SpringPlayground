<template>
  <div class="chat-view">
    <!-- Header -->
    <header class="chat-header">
      <h1 class="app-title">Chat App V2</h1>
      <div class="header-actions">
        <span class="connection-status" :class="connectionStatusClass">
          {{ connectionStatusText }}
        </span>
        <span class="user-info">{{ userStore.username }}</span>
        <button @click="handleLogout" class="logout-button">Logout</button>
      </div>
    </header>

    <!-- Main Content -->
    <div class="chat-content">
      <!-- Sidebar with Channel List -->
      <aside class="sidebar">
        <ChannelList />
      </aside>

      <!-- Chat Area -->
      <main class="chat-main">
        <div class="channel-header">
          <h2 class="channel-name">{{ currentChannelName }}</h2>
          <span class="channel-info">
            {{ messageStore.messageCount }} messages
          </span>
        </div>

        <MessageList />
        <MessageInput />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useMessagesStore } from '../stores/messages'
import { useWebSocketStore } from '../stores/websocket'
import websocketService from '../services/websocket.service'
import chatService from '../services/chat.service'
import ChannelList from '../components/ChannelList.vue'
import MessageList from '../components/MessageList.vue'
import MessageInput from '../components/MessageInput.vue'

const router = useRouter()
const userStore = useUserStore()
const messageStore = useMessagesStore()
const wsStore = useWebSocketStore()

const currentChannelName = computed(() => {
  const channelId = messageStore.currentChannel
  if (channelId.startsWith('group:')) {
    return channelId.replace('group:', '').charAt(0).toUpperCase() +
           channelId.replace('group:', '').slice(1)
  }
  return channelId
})

const connectionStatusClass = computed(() => {
  return {
    'status-connected': wsStore.isConnected,
    'status-connecting': wsStore.connectionStatus === 'connecting',
    'status-disconnected': !wsStore.isConnected && wsStore.connectionStatus === 'disconnected',
    'status-error': wsStore.connectionStatus === 'error'
  }
})

const connectionStatusText = computed(() => {
  switch (wsStore.connectionStatus) {
    case 'connected':
      return 'Connected'
    case 'connecting':
      return 'Connecting...'
    case 'error':
      return 'Connection Error'
    default:
      return 'Disconnected'
  }
})

async function connectWebSocket() {
  try {
    await websocketService.connect(userStore.userId)

    // Subscribe to current channel
    const channelId = messageStore.currentChannel
    websocketService.subscribeToChannel(channelId, handleIncomingMessage)

    // Load message history
    await loadMessageHistory(channelId)
  } catch (error) {
    console.error('Failed to connect WebSocket:', error)
  }
}

function handleIncomingMessage(message) {
  console.log('Received message:', message)
  messageStore.addMessage(message)
}

async function loadMessageHistory(channelId) {
  try {
    const messages = await chatService.fetchMessageHistory(channelId)
    if (messages && messages.length > 0) {
      messageStore.setMessages(messages)
    }
  } catch (error) {
    console.error('Error loading message history:', error)
  }
}

function handleLogout() {
  // Disconnect WebSocket
  websocketService.disconnect()

  // Clear messages
  messageStore.clearMessages()

  // Logout user
  userStore.logout()

  // Redirect to login
  router.push('/login')
}

onMounted(() => {
  // Connect to WebSocket when component mounts
  connectWebSocket()
})

onUnmounted(() => {
  // Cleanup on unmount
  websocketService.disconnect()
})
</script>

<style scoped>
.chat-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.app-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.connection-status {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  background-color: rgba(255, 255, 255, 0.2);
}

.status-connected {
  background-color: #4caf50;
}

.status-connecting {
  background-color: #ff9800;
}

.status-disconnected {
  background-color: #757575;
}

.status-error {
  background-color: #f44336;
}

.user-info {
  font-size: 14px;
  font-weight: 500;
}

.logout-button {
  padding: 8px 16px;
  background-color: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.logout-button:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.chat-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 260px;
  background-color: #2c2f33;
  color: white;
  overflow-y: auto;
  border-right: 1px solid #23272a;
}

.chat-main {
  display: flex;
  flex-direction: column;
  flex: 1;
  background-color: white;
}

.channel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 2px solid #e0e0e0;
  background-color: white;
}

.channel-name {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.channel-info {
  font-size: 12px;
  color: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }

  .chat-header {
    padding: 12px 16px;
  }

  .app-title {
    font-size: 20px;
  }

  .header-actions {
    gap: 12px;
  }
}
</style>
