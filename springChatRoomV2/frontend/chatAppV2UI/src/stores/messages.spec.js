import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useMessagesStore } from './messages'

describe('Messages Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  describe('Initial State', () => {
    it('should have correct initial state', () => {
      const store = useMessagesStore()

      expect(store.messages).toEqual([])
      expect(store.currentChannel).toBe('group:general')
      expect(store.messageCount).toBe(0)
    })
  })

  describe('addMessage', () => {
    it('should add a new message', () => {
      const store = useMessagesStore()
      const message = {
        id: 'msg-1',
        senderId: 'user-1',
        senderName: 'Alice',
        content: 'Hello world',
        timestamp: new Date().toISOString()
      }

      store.addMessage(message)

      expect(store.messages).toHaveLength(1)
      expect(store.messages[0].content).toBe('Hello world')
      expect(store.messageCount).toBe(1)
    })

    it('should not add duplicate messages', () => {
      const store = useMessagesStore()
      const message = {
        id: 'msg-1',
        senderId: 'user-1',
        senderName: 'Alice',
        content: 'Hello world'
      }

      store.addMessage(message)
      store.addMessage(message) // Try to add again

      expect(store.messages).toHaveLength(1)
    })

    it('should generate ID if not provided', () => {
      const store = useMessagesStore()
      const message = {
        senderId: 'user-1',
        senderName: 'Alice',
        content: 'No ID provided'
      }

      store.addMessage(message)

      expect(store.messages[0].id).toBeDefined()
      expect(store.messages[0].id).not.toBe('')
    })

    it('should set default messageType to TEXT', () => {
      const store = useMessagesStore()
      const message = {
        id: 'msg-1',
        senderId: 'user-1',
        senderName: 'Alice',
        content: 'Hello'
      }

      store.addMessage(message)

      expect(store.messages[0].messageType).toBe('TEXT')
    })
  })

  describe('setMessages', () => {
    it('should replace all messages', () => {
      const store = useMessagesStore()

      // Add initial message
      store.addMessage({ id: 'msg-1', content: 'First', senderId: 'u1', senderName: 'A' })

      // Set new messages
      const newMessages = [
        { id: 'msg-2', content: 'Second', senderId: 'u2', senderName: 'B' },
        { id: 'msg-3', content: 'Third', senderId: 'u3', senderName: 'C' }
      ]

      store.setMessages(newMessages)

      expect(store.messages).toHaveLength(2)
      expect(store.messages[0].id).toBe('msg-2')
      expect(store.messages[1].id).toBe('msg-3')
    })

    it('should format messages correctly', () => {
      const store = useMessagesStore()
      const messages = [
        { id: 'msg-1', content: 'Test', senderId: 'u1', senderName: 'User1' }
      ]

      store.setMessages(messages)

      expect(store.messages[0].messageType).toBe('TEXT')
      expect(store.messages[0].timestamp).toBeDefined()
    })
  })

  describe('clearMessages', () => {
    it('should clear all messages', () => {
      const store = useMessagesStore()

      store.addMessage({ id: 'msg-1', content: 'Test', senderId: 'u1', senderName: 'A' })
      store.addMessage({ id: 'msg-2', content: 'Test2', senderId: 'u2', senderName: 'B' })

      expect(store.messages).toHaveLength(2)

      store.clearMessages()

      expect(store.messages).toHaveLength(0)
      expect(store.messageCount).toBe(0)
    })
  })

  describe('setCurrentChannel', () => {
    it('should update current channel', () => {
      const store = useMessagesStore()

      store.setCurrentChannel('group:tech')

      expect(store.currentChannel).toBe('group:tech')
    })
  })

  describe('updateMessage', () => {
    it('should update message content', () => {
      const store = useMessagesStore()

      store.addMessage({
        id: 'msg-1',
        content: 'Original',
        senderId: 'u1',
        senderName: 'Alice'
      })

      store.updateMessage('msg-1', { content: 'Updated' })

      expect(store.messages[0].content).toBe('Updated')
    })

    it('should set edited flag and timestamp', () => {
      const store = useMessagesStore()

      store.addMessage({
        id: 'msg-1',
        content: 'Original',
        senderId: 'u1',
        senderName: 'Alice'
      })

      const beforeUpdate = new Date()
      store.updateMessage('msg-1', { content: 'Edited content' })

      expect(store.messages[0].edited).toBe(true)
      expect(store.messages[0].editedAt).toBeDefined()
      expect(new Date(store.messages[0].editedAt)).toBeInstanceOf(Date)
      expect(new Date(store.messages[0].editedAt).getTime()).toBeGreaterThanOrEqual(beforeUpdate.getTime())
    })

    it('should not update non-existent message', () => {
      const store = useMessagesStore()

      store.addMessage({
        id: 'msg-1',
        content: 'Original',
        senderId: 'u1',
        senderName: 'Alice'
      })

      store.updateMessage('non-existent-id', { content: 'Should not work' })

      expect(store.messages[0].content).toBe('Original')
      expect(store.messages[0].edited).toBeUndefined()
    })
  })

  describe('deleteMessage', () => {
    it('should delete message by id', () => {
      const store = useMessagesStore()

      store.addMessage({ id: 'msg-1', content: 'First', senderId: 'u1', senderName: 'A' })
      store.addMessage({ id: 'msg-2', content: 'Second', senderId: 'u2', senderName: 'B' })
      store.addMessage({ id: 'msg-3', content: 'Third', senderId: 'u3', senderName: 'C' })

      expect(store.messages).toHaveLength(3)

      store.deleteMessage('msg-2')

      expect(store.messages).toHaveLength(2)
      expect(store.messages.find(m => m.id === 'msg-2')).toBeUndefined()
      expect(store.messages.find(m => m.id === 'msg-1')).toBeDefined()
      expect(store.messages.find(m => m.id === 'msg-3')).toBeDefined()
    })

    it('should not error when deleting non-existent message', () => {
      const store = useMessagesStore()

      store.addMessage({ id: 'msg-1', content: 'Test', senderId: 'u1', senderName: 'A' })

      expect(() => {
        store.deleteMessage('non-existent-id')
      }).not.toThrow()

      expect(store.messages).toHaveLength(1)
    })
  })

  describe('sortedMessages', () => {
    it('should return messages sorted by timestamp', () => {
      const store = useMessagesStore()

      const now = new Date()
      store.addMessage({
        id: 'msg-3',
        content: 'Third',
        senderId: 'u1',
        senderName: 'A',
        timestamp: new Date(now.getTime() + 2000).toISOString()
      })
      store.addMessage({
        id: 'msg-1',
        content: 'First',
        senderId: 'u2',
        senderName: 'B',
        timestamp: now.toISOString()
      })
      store.addMessage({
        id: 'msg-2',
        content: 'Second',
        senderId: 'u3',
        senderName: 'C',
        timestamp: new Date(now.getTime() + 1000).toISOString()
      })

      const sorted = store.sortedMessages

      expect(sorted[0].id).toBe('msg-1')
      expect(sorted[1].id).toBe('msg-2')
      expect(sorted[2].id).toBe('msg-3')
    })
  })
})
