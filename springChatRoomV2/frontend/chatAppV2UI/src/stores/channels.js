import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import chatService from '../services/chat.service'

export const useChannelsStore = defineStore('channels', () => {
  // State
  const channels = ref([])
  const currentChannelId = ref(null)  // Will be set when channels load
  const isLoading = ref(false)
  const error = ref(null)

  // Getters
  const currentChannel = computed(() => {
    return channels.value.find(c => c.id === currentChannelId.value) || null
  })

  const groupChannels = computed(() => {
    return channels.value.filter(c => c.type === 'GROUP')
  })

  const directChannels = computed(() => {
    return channels.value.filter(c => c.type === 'DIRECT')
  })

  const getChannelById = computed(() => {
    return (channelId) => channels.value.find(c => c.id === channelId)
  })

  // Actions
  function setCurrentChannel(channelId) {
    currentChannelId.value = channelId
  }

  function addChannel(channel) {
    const exists = channels.value.some(c => c.id === channel.id)
    if (!exists) {
      channels.value.push({
        id: channel.id,
        name: channel.name,
        type: channel.type || 'GROUP',
        members: channel.members || [],
        unreadCount: channel.unreadCount || 0,
        lastMessage: channel.lastMessage || null,
        createdAt: channel.createdAt || new Date().toISOString()
      })
    }
  }

  function updateChannel(channelId, updates) {
    const index = channels.value.findIndex(c => c.id === channelId)
    if (index !== -1) {
      channels.value[index] = {
        ...channels.value[index],
        ...updates
      }
    }
  }

  function removeChannel(channelId) {
    const index = channels.value.findIndex(c => c.id === channelId)
    if (index !== -1) {
      channels.value.splice(index, 1)
    }
  }

  function incrementUnreadCount(channelId) {
    const channel = channels.value.find(c => c.id === channelId)
    if (channel && channel.id !== currentChannelId.value) {
      channel.unreadCount = (channel.unreadCount || 0) + 1
    }
  }

  function clearUnreadCount(channelId) {
    const channel = channels.value.find(c => c.id === channelId)
    if (channel) {
      channel.unreadCount = 0
    }
  }

  function setChannels(newChannels) {
    channels.value = newChannels.map(ch => ({
      id: ch.id,
      name: ch.name,
      type: ch.type || 'GROUP',
      members: ch.members || [],
      unreadCount: ch.unreadCount || 0,
      lastMessage: ch.lastMessage || null,
      createdAt: ch.createdAt || new Date().toISOString()
    }))
  }

  async function loadUserChannels() {
    isLoading.value = true
    error.value = null
    try {
      const data = await chatService.getUserChannels()
      if (data && data.length > 0) {
        setChannels(data)
        // Set first channel as current if not already set
        if (!currentChannelId.value && data.length > 0) {
          currentChannelId.value = data[0].id
        }
      } else {
        // No channels returned from backend
        console.warn('No channels available for user')
        channels.value = []
        currentChannelId.value = null
        error.value = 'No channels available. Create a channel to get started.'
      }
    } catch (err) {
      console.error('Error loading channels:', err)
      error.value = 'Failed to load channels'
      channels.value = []
      currentChannelId.value = null
    } finally {
      isLoading.value = false
    }
  }

  async function createGroupChannel(name, memberIds) {
    isLoading.value = true
    error.value = null
    try {
      const channel = await chatService.createGroupChannel(name, memberIds)
      addChannel(channel)
      return channel
    } catch (err) {
      console.error('Error creating group channel:', err)
      error.value = 'Failed to create channel'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  async function createDirectChannel(userId1, userId2) {
    isLoading.value = true
    error.value = null
    try {
      const channel = await chatService.createDirectChannel(userId1, userId2)
      addChannel(channel)
      return channel
    } catch (err) {
      console.error('Error creating direct channel:', err)
      error.value = 'Failed to create direct message'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  function clearError() {
    error.value = null
  }

  return {
    channels,
    currentChannelId,
    currentChannel,
    groupChannels,
    directChannels,
    isLoading,
    error,
    getChannelById,
    setCurrentChannel,
    addChannel,
    updateChannel,
    removeChannel,
    incrementUnreadCount,
    clearUnreadCount,
    setChannels,
    loadUserChannels,
    createGroupChannel,
    createDirectChannel,
    clearError
  }
})
