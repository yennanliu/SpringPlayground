import { defineStore } from 'pinia'
import { authService } from '@/services'
import type { User, SignupData, AuthResponse } from '@/types'
import { AxiosError } from 'axios'

interface AuthState {
  token: string | null
  user: User | null
  loading: boolean
  error: string | null
}

interface ApiErrorResponse {
  message?: string
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: localStorage.getItem('token') || null,
    user: null,
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state): boolean => !!state.token,
    getToken: (state): string | null => state.token,
    getUser: (state): User | null => state.user
  },

  actions: {
    async signin(email: string, password: string): Promise<AuthResponse> {
      this.loading = true
      this.error = null
      try {
        const response = await authService.signin(email, password)
        this.token = response.token
        authService.setToken(response.token)
        return response
      } catch (error) {
        const axiosError = error as AxiosError<ApiErrorResponse>
        this.error = axiosError.response?.data?.message || 'Login failed'
        throw error
      } finally {
        this.loading = false
      }
    },

    async signup(userData: SignupData): Promise<AuthResponse> {
      this.loading = true
      this.error = null
      try {
        const response = await authService.signup(userData)
        return response
      } catch (error) {
        const axiosError = error as AxiosError<ApiErrorResponse>
        this.error = axiosError.response?.data?.message || 'Signup failed'
        throw error
      } finally {
        this.loading = false
      }
    },

    signout(): void {
      this.token = null
      this.user = null
      authService.signout()
    },

    checkAuth(): boolean {
      const token = authService.getToken()
      this.token = token
      return !!token
    }
  }
})
