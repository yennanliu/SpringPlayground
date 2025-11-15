import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useWebSocketStore = defineStore('websocket', () => {
  // State
  const isConnected = ref(false)
  const connectionError = ref(null)
  const connectionStatus = ref('disconnected') // 'disconnected', 'connecting', 'connected', 'error'

  // Actions
  function setConnectionStatus(status) {
    connectionStatus.value = status
    isConnected.value = status === 'connected'
  }

  function setError(error) {
    connectionError.value = error
    if (error) {
      connectionStatus.value = 'error'
    }
  }

  function clearError() {
    connectionError.value = null
  }

  return {
    isConnected,
    connectionError,
    connectionStatus,
    setConnectionStatus,
    setError,
    clearError
  }
})
