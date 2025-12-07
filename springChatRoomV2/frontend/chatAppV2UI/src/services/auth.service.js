import { apiClient } from './chat.service'

/**
 * Authentication Service
 * Handles user registration, login, and token management
 */
class AuthService {
  /**
   * Register a new user
   * @param {Object} userData - User registration data
   * @param {string} userData.username - Username (3-20 characters)
   * @param {string} userData.email - Email address
   * @param {string} userData.password - Password (minimum 6 characters)
   * @returns {Promise<Object>} Response containing token and user data
   */
  async register(userData) {
    try {
      const response = await apiClient.post('/api/auth/register', {
        username: userData.username,
        email: userData.email,
        password: userData.password
      })

      // Store JWT token
      if (response.data.token) {
        localStorage.setItem('authToken', response.data.token)
      }

      return response.data
    } catch (error) {
      console.error('Registration error:', error)
      throw this.handleAuthError(error)
    }
  }

  /**
   * Login user with credentials
   * @param {Object} credentials - Login credentials
   * @param {string} credentials.username - Username
   * @param {string} credentials.password - Password
   * @returns {Promise<Object>} Response containing token and user data
   */
  async login(credentials) {
    try {
      const response = await apiClient.post('/api/auth/login', {
        username: credentials.username,
        password: credentials.password
      })

      // Store JWT token
      if (response.data.token) {
        localStorage.setItem('authToken', response.data.token)
      }

      return response.data
    } catch (error) {
      console.error('Login error:', error)
      throw this.handleAuthError(error)
    }
  }

  /**
   * Logout user and clear stored token
   */
  logout() {
    localStorage.removeItem('authToken')
  }

  /**
   * Get stored JWT token
   * @returns {string|null} JWT token or null if not found
   */
  getToken() {
    return localStorage.getItem('authToken')
  }

  /**
   * Check if user has a valid token
   * Note: This only checks if token exists, not if it's expired
   * @returns {boolean} True if token exists
   */
  hasToken() {
    return !!this.getToken()
  }

  /**
   * Handle authentication errors with user-friendly messages
   * @param {Error} error - Error from API call
   * @returns {Error} Error with user-friendly message
   */
  handleAuthError(error) {
    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      switch (status) {
        case 400:
          return new Error('Invalid input. Please check your information.')
        case 401:
          return new Error('Invalid username or password.')
        case 404:
          return new Error('User not found.')
        case 409:
          return new Error('Username or email already exists.')
        case 500:
          return new Error('Server error. Please try again later.')
        default:
          return new Error(message || 'Authentication failed. Please try again.')
      }
    } else if (error.request) {
      return new Error('Cannot connect to server. Please check your connection.')
    } else {
      return new Error('An unexpected error occurred.')
    }
  }
}

export default new AuthService()
