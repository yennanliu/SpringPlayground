import axios from 'axios'

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000
})

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    // Add auth token if available
    const token = localStorage.getItem('authToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    console.error('Response error:', error)

    // Handle specific error cases
    if (error.response) {
      // Server responded with error status
      const status = error.response.status
      const message = error.response.data?.message || error.message

      switch (status) {
        case 401:
          console.error('Unauthorized - please login')
          // Could redirect to login here
          break
        case 403:
          console.error('Forbidden - insufficient permissions')
          break
        case 404:
          console.error('Resource not found')
          break
        case 500:
          console.error('Server error')
          break
        default:
          console.error(`Error ${status}: ${message}`)
      }
    } else if (error.request) {
      // Request made but no response received
      console.error('No response from server')
    } else {
      // Error setting up the request
      console.error('Error:', error.message)
    }

    return Promise.reject(error)
  }
)

class ChatService {
  /**
   * Fetch message history for a channel
   * @param {string} channelId - Channel ID
   * @param {number} page - Page number (default: 0)
   * @param {number} size - Page size (default: 50)
   * @returns {Promise<Array>} Array of messages
   */
  async fetchMessageHistory(channelId, page = 0, size = 50) {
    try {
      const response = await apiClient.get(`/api/channels/${channelId}/messages`, {
        params: { page, size }
      })
      return response.data
    } catch (error) {
      console.error('Error fetching message history:', error)
      // Return empty array if API not yet implemented
      return []
    }
  }

  /**
   * Get user's channels
   * @returns {Promise<Array>} Array of channels
   */
  async getUserChannels() {
    try {
      const response = await apiClient.get('/api/channels')
      return response.data
    } catch (error) {
      console.error('Error fetching channels:', error)
      return []
    }
  }

  /**
   * Create a group channel
   * @param {string} name - Channel name
   * @param {Array<string>} memberIds - Array of user IDs
   * @returns {Promise<Object>} Created channel
   */
  async createGroupChannel(name, memberIds) {
    try {
      const response = await apiClient.post('/api/channels/group', {
        name,
        memberIds
      })
      return response.data
    } catch (error) {
      console.error('Error creating group channel:', error)
      throw error
    }
  }

  /**
   * Create or get a direct message channel
   * @param {string} userId1 - First user ID
   * @param {string} userId2 - Second user ID
   * @returns {Promise<Object>} Direct message channel
   */
  async createDirectChannel(userId1, userId2) {
    try {
      const response = await apiClient.post('/api/channels/direct', {
        userId1,
        userId2
      })
      return response.data
    } catch (error) {
      console.error('Error creating direct channel:', error)
      throw error
    }
  }
}

export default new ChatService()
export { apiClient }
