import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from './user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  describe('Initial State', () => {
    it('should have correct initial state', () => {
      const store = useUserStore()

      expect(store.userId).toBe('')
      expect(store.username).toBe('')
      expect(store.isAuthenticated).toBe(false)
      expect(store.currentUser).toBeNull()
    })
  })

  describe('login', () => {
    it('should login user with basic data', () => {
      const store = useUserStore()
      const userData = {
        username: 'testuser',
        userId: 'user-123'
      }

      store.login(userData)

      expect(store.isAuthenticated).toBe(true)
      expect(store.username).toBe('testuser')
      expect(store.userId).toBe('user-123')
      expect(store.currentUser).toBeDefined()
      expect(store.currentUser.username).toBe('testuser')
    })

    it('should login user with full profile data', () => {
      const store = useUserStore()
      const userData = {
        username: 'testuser',
        userId: 'user-123',
        email: 'test@example.com',
        displayName: 'Test User',
        avatarUrl: 'https://example.com/avatar.jpg',
        statusMessage: 'Hello world'
      }

      store.login(userData)

      expect(store.currentUser.email).toBe('test@example.com')
      expect(store.currentUser.displayName).toBe('Test User')
      expect(store.currentUser.avatarUrl).toBe('https://example.com/avatar.jpg')
      expect(store.currentUser.statusMessage).toBe('Hello world')
      expect(store.currentUser.isOnline).toBe(true)
    })

    it('should save user data to localStorage', () => {
      const store = useUserStore()
      const userData = {
        username: 'testuser',
        userId: 'user-123',
        email: 'test@example.com'
      }

      store.login(userData)

      const savedData = JSON.parse(localStorage.getItem('chatUser'))
      expect(savedData).toBeDefined()
      expect(savedData.username).toBe('testuser')
      expect(savedData.email).toBe('test@example.com')
    })
  })

  describe('logout', () => {
    it('should clear user state on logout', () => {
      const store = useUserStore()

      // Login first
      store.login({ username: 'testuser', userId: 'user-123' })
      expect(store.isAuthenticated).toBe(true)

      // Logout
      store.logout()

      expect(store.isAuthenticated).toBe(false)
      expect(store.userId).toBe('')
      expect(store.username).toBe('')
      expect(store.currentUser).toBeNull()
    })

    it('should remove user data from localStorage', () => {
      const store = useUserStore()

      store.login({ username: 'testuser', userId: 'user-123' })
      expect(localStorage.getItem('chatUser')).toBeDefined()

      store.logout()
      expect(localStorage.getItem('chatUser')).toBeNull()
    })
  })

  describe('updateProfile', () => {
    it('should update profile fields', () => {
      const store = useUserStore()

      // Login first
      store.login({ username: 'testuser', userId: 'user-123' })

      // Update profile
      store.updateProfile({
        displayName: 'New Name',
        email: 'new@example.com',
        statusMessage: 'New status'
      })

      expect(store.currentUser.displayName).toBe('New Name')
      expect(store.currentUser.email).toBe('new@example.com')
      expect(store.currentUser.statusMessage).toBe('New status')
    })

    it('should persist profile updates to localStorage', () => {
      const store = useUserStore()

      store.login({ username: 'testuser', userId: 'user-123' })
      store.updateProfile({
        displayName: 'Updated Name',
        avatarUrl: 'https://example.com/new-avatar.jpg'
      })

      const savedData = JSON.parse(localStorage.getItem('chatUser'))
      expect(savedData.displayName).toBe('Updated Name')
      expect(savedData.avatarUrl).toBe('https://example.com/new-avatar.jpg')
    })

    it('should not update if user is not logged in', () => {
      const store = useUserStore()

      store.updateProfile({ displayName: 'Should not work' })

      expect(store.currentUser).toBeNull()
    })
  })

  describe('loadFromLocalStorage', () => {
    it('should restore user data from localStorage on init', () => {
      const userData = {
        id: 'user-123',
        username: 'testuser',
        email: 'test@example.com',
        displayName: 'Test User'
      }
      localStorage.setItem('chatUser', JSON.stringify(userData))

      const store = useUserStore()

      expect(store.isAuthenticated).toBe(true)
      expect(store.username).toBe('testuser')
      expect(store.userId).toBe('user-123')
      expect(store.currentUser.email).toBe('test@example.com')
    })

    it('should handle corrupted localStorage data gracefully', () => {
      localStorage.setItem('chatUser', 'invalid-json')

      const store = useUserStore()

      expect(store.isAuthenticated).toBe(false)
      expect(store.currentUser).toBeNull()
    })
  })
})
