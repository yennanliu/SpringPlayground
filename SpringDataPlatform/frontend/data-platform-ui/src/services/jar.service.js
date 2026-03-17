import api from './api'

/**
 * Service for JAR file management operations
 */
const jarService = {
  /**
   * Get all JAR files
   * @returns {Promise<Array>} List of JAR files
   */
  async getAll() {
    const response = await api.get('/jar/')
    return response.data
  },

  /**
   * Get a JAR file by ID
   * @param {string|number} id - JAR file ID
   * @returns {Promise<Object>} JAR file data
   */
  async getById(id) {
    const response = await api.get(`/jar/${id}`)
    return response.data
  },

  /**
   * Upload a new JAR file
   * @param {File} file - JAR file to upload
   * @returns {Promise<Object>} Upload response
   */
  async create(file) {
    const formData = new FormData()
    formData.append('jarfile', file)

    const response = await api.post('/jar/add_jar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  /**
   * Delete a JAR file by ID
   * @param {string|number} id - JAR file ID
   * @returns {Promise<Object>} Delete response
   */
  async delete(id) {
    const response = await api.delete(`/jar/${id}`)
    return response.data
  }
}

export default jarService
