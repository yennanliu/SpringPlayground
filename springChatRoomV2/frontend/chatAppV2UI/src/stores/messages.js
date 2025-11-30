import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMessagesStore = defineStore('messages', () => {
  // State
  const messages = ref([])
  const currentChannel = ref('group:general')

  // Getters
  const sortedMessages = computed(() => {
    return [...messages.value].sort((a, b) =>
      new Date(a.timestamp) - new Date(b.timestamp)
    )
  })

  const messageCount = computed(() => messages.value.length)

  // Actions
  function addMessage(message) {
    // Avoid duplicates
    const exists = messages.value.some(m => m.id === message.id)
    if (!exists) {
      messages.value.push({
        id: message.id || Date.now().toString(),
        senderId: message.senderId,
        senderName: message.senderName,
        content: message.content,
        timestamp: message.timestamp || new Date().toISOString(),
        messageType: message.messageType || 'TEXT'
      })
    }
  }

  function setMessages(newMessages) {
    messages.value = newMessages.map(msg => ({
      id: msg.id || Date.now().toString(),
      senderId: msg.senderId,
      senderName: msg.senderName,
      content: msg.content,
      timestamp: msg.timestamp || new Date().toISOString(),
      messageType: msg.messageType || 'TEXT'
    }))
  }

  function clearMessages() {
    messages.value = []
  }

  function setCurrentChannel(channelId) {
    currentChannel.value = channelId
  }

  function updateMessage(messageId, updates) {
    const index = messages.value.findIndex(m => m.id === messageId)
    if (index !== -1) {
      messages.value[index] = {
        ...messages.value[index],
        ...updates,
        edited: true,
        editedAt: new Date().toISOString()
      }
    }
  }

  function deleteMessage(messageId) {
    const index = messages.value.findIndex(m => m.id === messageId)
    if (index !== -1) {
      messages.value.splice(index, 1)
    }
  }

  return {
    messages,
    currentChannel,
    sortedMessages,
    messageCount,
    addMessage,
    setMessages,
    clearMessages,
    setCurrentChannel,
    updateMessage,
    deleteMessage
  }
})
