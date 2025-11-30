import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // State
  const currentUser = ref(null)

  // Getters
  const isAuthenticated = computed(() => currentUser.value !== null)
  const userId = computed(() => currentUser.value?.id || null)
  const username = computed(() => currentUser.value?.username || '')

  // Actions
  function login(userData) {
    currentUser.value = {
      id: userData.id || Date.now().toString(),
      username: userData.username,
      email: userData.email || '',
      displayName: userData.displayName || '',
      avatarUrl: userData.avatarUrl || null,
      statusMessage: userData.statusMessage || '',
      isOnline: true
    }
    // Store in localStorage for persistence
    localStorage.setItem('currentUser', JSON.stringify(currentUser.value))
  }

  function logout() {
    currentUser.value = null
    localStorage.removeItem('currentUser')
  }

  function loadUserFromStorage() {
    const stored = localStorage.getItem('currentUser')
    if (stored) {
      try {
        currentUser.value = JSON.parse(stored)
      } catch (e) {
        console.error('Failed to parse stored user:', e)
        localStorage.removeItem('currentUser')
      }
    }
  }

  function updateProfile(updates) {
    if (currentUser.value) {
      currentUser.value = {
        ...currentUser.value,
        ...updates
      }
      localStorage.setItem('currentUser', JSON.stringify(currentUser.value))
    }
  }

  return {
    currentUser,
    isAuthenticated,
    userId,
    username,
    login,
    logout,
    loadUserFromStorage,
    updateProfile
  }
})
