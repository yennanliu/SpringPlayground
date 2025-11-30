import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useChannelsStore } from './channels'
import chatService from '../services/chat.service'

vi.mock('../services/chat.service')

describe('Channels Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  describe('Initial State', () => {
    it('should have correct initial state', () => {
      const store = useChannelsStore()

      expect(store.channels).toEqual([])
      expect(store.currentChannelId).toBe('group:general')
      expect(store.isLoading).toBe(false)
      expect(store.error).toBeNull()
    })
  })

  describe('Computed Properties', () => {
    it('currentChannel should return current channel object', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:general', name: 'general', type: 'GROUP' })
      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })
      store.setCurrentChannel('group:tech')

      expect(store.currentChannel.id).toBe('group:tech')
      expect(store.currentChannel.name).toBe('tech')
    })

    it('groupChannels should filter GROUP channels', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:general', name: 'general', type: 'GROUP' })
      store.addChannel({ id: 'dm:user1:user2', name: 'DM', type: 'DIRECT' })
      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })

      const groups = store.groupChannels

      expect(groups).toHaveLength(2)
      expect(groups.every(c => c.type === 'GROUP')).toBe(true)
    })

    it('directChannels should filter DIRECT channels', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:general', name: 'general', type: 'GROUP' })
      store.addChannel({ id: 'dm:user1:user2', name: 'DM1', type: 'DIRECT' })
      store.addChannel({ id: 'dm:user1:user3', name: 'DM2', type: 'DIRECT' })

      const directs = store.directChannels

      expect(directs).toHaveLength(2)
      expect(directs.every(c => c.type === 'DIRECT')).toBe(true)
    })

    it('getChannelById should return channel by id', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })

      const channel = store.getChannelById('group:tech')

      expect(channel).toBeDefined()
      expect(channel.name).toBe('tech')
    })
  })

  describe('addChannel', () => {
    it('should add a new channel', () => {
      const store = useChannelsStore()
      const channel = {
        id: 'group:tech',
        name: 'tech',
        type: 'GROUP'
      }

      store.addChannel(channel)

      expect(store.channels).toHaveLength(1)
      expect(store.channels[0].id).toBe('group:tech')
    })

    it('should not add duplicate channels', () => {
      const store = useChannelsStore()
      const channel = { id: 'group:tech', name: 'tech', type: 'GROUP' }

      store.addChannel(channel)
      store.addChannel(channel)

      expect(store.channels).toHaveLength(1)
    })

    it('should set default values', () => {
      const store = useChannelsStore()
      const channel = { id: 'group:tech', name: 'tech' }

      store.addChannel(channel)

      expect(store.channels[0].type).toBe('GROUP')
      expect(store.channels[0].members).toEqual([])
      expect(store.channels[0].unreadCount).toBe(0)
      expect(store.channels[0].lastMessage).toBeNull()
      expect(store.channels[0].createdAt).toBeDefined()
    })
  })

  describe('updateChannel', () => {
    it('should update channel properties', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })
      store.updateChannel('group:tech', { name: 'technology', unreadCount: 5 })

      const channel = store.getChannelById('group:tech')
      expect(channel.name).toBe('technology')
      expect(channel.unreadCount).toBe(5)
    })

    it('should not error for non-existent channel', () => {
      const store = useChannelsStore()

      expect(() => {
        store.updateChannel('non-existent', { name: 'test' })
      }).not.toThrow()
    })
  })

  describe('removeChannel', () => {
    it('should remove channel by id', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })
      store.addChannel({ id: 'group:general', name: 'general', type: 'GROUP' })

      expect(store.channels).toHaveLength(2)

      store.removeChannel('group:tech')

      expect(store.channels).toHaveLength(1)
      expect(store.getChannelById('group:tech')).toBeUndefined()
    })
  })

  describe('Unread Count Management', () => {
    it('incrementUnreadCount should increment unread count', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })
      store.setCurrentChannel('group:general')

      store.incrementUnreadCount('group:tech')

      expect(store.getChannelById('group:tech').unreadCount).toBe(1)

      store.incrementUnreadCount('group:tech')
      expect(store.getChannelById('group:tech').unreadCount).toBe(2)
    })

    it('should not increment unread count for current channel', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP' })
      store.setCurrentChannel('group:tech')

      store.incrementUnreadCount('group:tech')

      expect(store.getChannelById('group:tech').unreadCount).toBe(0)
    })

    it('clearUnreadCount should reset unread count to 0', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:tech', name: 'tech', type: 'GROUP', unreadCount: 5 })

      store.clearUnreadCount('group:tech')

      expect(store.getChannelById('group:tech').unreadCount).toBe(0)
    })
  })

  describe('setChannels', () => {
    it('should replace all channels', () => {
      const store = useChannelsStore()

      store.addChannel({ id: 'group:old', name: 'old', type: 'GROUP' })

      const newChannels = [
        { id: 'group:tech', name: 'tech', type: 'GROUP' },
        { id: 'group:general', name: 'general', type: 'GROUP' }
      ]

      store.setChannels(newChannels)

      expect(store.channels).toHaveLength(2)
      expect(store.getChannelById('group:old')).toBeUndefined()
      expect(store.getChannelById('group:tech')).toBeDefined()
    })
  })

  describe('loadUserChannels', () => {
    it('should load channels from API', async () => {
      const store = useChannelsStore()
      const mockChannels = [
        { id: 'group:tech', name: 'tech', type: 'GROUP' },
        { id: 'group:general', name: 'general', type: 'GROUP' }
      ]

      chatService.getUserChannels.mockResolvedValue(mockChannels)

      await store.loadUserChannels()

      expect(store.channels).toHaveLength(2)
      expect(store.isLoading).toBe(false)
      expect(store.error).toBeNull()
    })

    it('should set default channel if API returns empty', async () => {
      const store = useChannelsStore()

      chatService.getUserChannels.mockResolvedValue([])

      await store.loadUserChannels()

      expect(store.channels).toHaveLength(1)
      expect(store.channels[0].id).toBe('group:general')
    })

    it('should handle API errors gracefully', async () => {
      const store = useChannelsStore()

      chatService.getUserChannels.mockRejectedValue(new Error('API Error'))

      await store.loadUserChannels()

      expect(store.error).toBe('Failed to load channels')
      expect(store.channels).toHaveLength(1)
      expect(store.channels[0].id).toBe('group:general')
      expect(store.isLoading).toBe(false)
    })
  })

  describe('createGroupChannel', () => {
    it('should create a group channel', async () => {
      const store = useChannelsStore()
      const mockChannel = { id: 'group:new', name: 'new', type: 'GROUP' }

      chatService.createGroupChannel.mockResolvedValue(mockChannel)

      const result = await store.createGroupChannel('new', ['user1', 'user2'])

      expect(result).toEqual(mockChannel)
      expect(store.channels).toHaveLength(1)
      expect(store.getChannelById('group:new')).toBeDefined()
    })

    it('should handle creation errors', async () => {
      const store = useChannelsStore()

      chatService.createGroupChannel.mockRejectedValue(new Error('API Error'))

      await expect(store.createGroupChannel('new', [])).rejects.toThrow()
      expect(store.error).toBe('Failed to create channel')
    })
  })

  describe('createDirectChannel', () => {
    it('should create a direct channel', async () => {
      const store = useChannelsStore()
      const mockChannel = { id: 'dm:user1:user2', name: 'DM', type: 'DIRECT' }

      chatService.createDirectChannel.mockResolvedValue(mockChannel)

      const result = await store.createDirectChannel('user1', 'user2')

      expect(result).toEqual(mockChannel)
      expect(store.directChannels).toHaveLength(1)
    })

    it('should handle creation errors', async () => {
      const store = useChannelsStore()

      chatService.createDirectChannel.mockRejectedValue(new Error('API Error'))

      await expect(store.createDirectChannel('user1', 'user2')).rejects.toThrow()
      expect(store.error).toBe('Failed to create direct message')
    })
  })

  describe('clearError', () => {
    it('should clear error state', () => {
      const store = useChannelsStore()

      store.error = 'Some error'
      expect(store.error).toBe('Some error')

      store.clearError()
      expect(store.error).toBeNull()
    })
  })
})
