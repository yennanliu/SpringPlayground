import { defineStore } from 'pinia'
import { authService } from '@/services'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: null,
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    getToken: (state) => state.token,
    getUser: (state) => state.user
  },

  actions: {
    async signin(email, password) {
      this.loading = true
      this.error = null
      try {
        const response = await authService.signin(email, password)
        this.token = response.token
        authService.setToken(response.token)
        return response
      } catch (error) {
        this.error = error.response?.data?.message || 'Login failed'
        throw error
      } finally {
        this.loading = false
      }
    },

    async signup(userData) {
      this.loading = true
      this.error = null
      try {
        const response = await authService.signup(userData)
        return response
      } catch (error) {
        this.error = error.response?.data?.message || 'Signup failed'
        throw error
      } finally {
        this.loading = false
      }
    },

    signout() {
      this.token = null
      this.user = null
      authService.signout()
    },

    checkAuth() {
      const token = authService.getToken()
      this.token = token
      return !!token
    }
  }
})
