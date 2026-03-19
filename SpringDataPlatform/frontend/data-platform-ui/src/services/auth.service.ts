import api from './api'
import type { AuthResponse, SignupData } from '@/types'

/**
 * Authentication service for user sign in/up/out operations
 */
const authService = {
  /**
   * Sign in user with email and password
   */
  async signin(email: string, password: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/users/signIn', { email, password })
    return response.data
  },

  /**
   * Register a new user
   */
  async signup(userData: SignupData): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/users/signup', userData)
    return response.data
  },

  /**
   * Sign out user - clears local storage
   */
  signout(): void {
    localStorage.removeItem('token')
  },

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token')
  },

  /**
   * Get current auth token
   */
  getToken(): string | null {
    return localStorage.getItem('token')
  },

  /**
   * Store auth token
   */
  setToken(token: string): void {
    localStorage.setItem('token', token)
  }
}

export default authService
