import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useTypingStore = defineStore('typing', () => {
  // State: Map of channelId -> Set of usernames typing
  const typingUsers = ref(new Map())
  const typingTimeouts = ref(new Map())

  // Getters
  const getTypingUsersForChannel = computed(() => {
    return (channelId) => {
      const users = typingUsers.value.get(channelId)
      return users ? Array.from(users) : []
    }
  })

  // Actions
  function addTypingUser(channelId, username) {
    if (!typingUsers.value.has(channelId)) {
      typingUsers.value.set(channelId, new Set())
    }

    const users = typingUsers.value.get(channelId)
    users.add(username)

    // Clear existing timeout for this user
    const timeoutKey = `${channelId}:${username}`
    const existingTimeout = typingTimeouts.value.get(timeoutKey)
    if (existingTimeout) {
      clearTimeout(existingTimeout)
    }

    // Auto-remove after 3 seconds of inactivity
    const timeout = setTimeout(() => {
      removeTypingUser(channelId, username)
    }, 3000)

    typingTimeouts.value.set(timeoutKey, timeout)

    // Force reactivity update
    typingUsers.value = new Map(typingUsers.value)
  }

  function removeTypingUser(channelId, username) {
    const users = typingUsers.value.get(channelId)
    if (users) {
      users.delete(username)
      if (users.size === 0) {
        typingUsers.value.delete(channelId)
      }

      // Force reactivity update
      typingUsers.value = new Map(typingUsers.value)
    }

    // Clear timeout
    const timeoutKey = `${channelId}:${username}`
    const timeout = typingTimeouts.value.get(timeoutKey)
    if (timeout) {
      clearTimeout(timeout)
      typingTimeouts.value.delete(timeoutKey)
    }
  }

  function clearTypingForChannel(channelId) {
    // Clear all timeouts for this channel
    typingUsers.value.get(channelId)?.forEach(username => {
      const timeoutKey = `${channelId}:${username}`
      const timeout = typingTimeouts.value.get(timeoutKey)
      if (timeout) {
        clearTimeout(timeout)
        typingTimeouts.value.delete(timeoutKey)
      }
    })

    typingUsers.value.delete(channelId)

    // Force reactivity update
    typingUsers.value = new Map(typingUsers.value)
  }

  function clearAllTyping() {
    // Clear all timeouts
    typingTimeouts.value.forEach(timeout => clearTimeout(timeout))
    typingTimeouts.value.clear()
    typingUsers.value.clear()
  }

  return {
    typingUsers,
    getTypingUsersForChannel,
    addTypingUser,
    removeTypingUser,
    clearTypingForChannel,
    clearAllTyping
  }
})
