import api from './api'

/**
 * Authentication service for user sign in/up/out operations
 */
const authService = {
  /**
   * Sign in user with email and password
   * @param {string} email - User email
   * @param {string} password - User password
   * @returns {Promise<{token: string}>} Response with auth token
   */
  async signin(email, password) {
    const response = await api.post('/users/signIn', { email, password })
    return response.data
  },

  /**
   * Register a new user
   * @param {Object} userData - User registration data
   * @param {string} userData.email - User email
   * @param {string} userData.firstName - User first name
   * @param {string} userData.lastName - User last name
   * @param {string} userData.password - User password
   * @returns {Promise<Object>} Response data
   */
  async signup(userData) {
    const response = await api.post('/users/signup', userData)
    return response.data
  },

  /**
   * Sign out user - clears local storage
   */
  signout() {
    localStorage.removeItem('token')
  },

  /**
   * Check if user is authenticated
   * @returns {boolean} True if token exists
   */
  isAuthenticated() {
    return !!localStorage.getItem('token')
  },

  /**
   * Get current auth token
   * @returns {string|null} Auth token or null
   */
  getToken() {
    return localStorage.getItem('token')
  },

  /**
   * Store auth token
   * @param {string} token - Auth token to store
   */
  setToken(token) {
    localStorage.setItem('token', token)
  }
}

export default authService
