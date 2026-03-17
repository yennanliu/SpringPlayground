import api from './api'

/**
 * Service for Zeppelin notebook operations
 */
const zeppelinService = {
  /**
   * Get all notebooks
   * @returns {Promise<Array>} List of notebooks
   */
  async getAll() {
    const response = await api.get('/zeppelin/')
    return response.data
  },

  /**
   * Get a notebook by ID
   * @param {string|number} id - Notebook ID
   * @returns {Promise<Object>} Notebook data
   */
  async getById(id) {
    const response = await api.get(`/zeppelin/${id}`)
    return response.data
  },

  /**
   * Create a new notebook
   * @param {Object} notebookData - Notebook configuration
   * @param {string} notebookData.notePath - Notebook path/name
   * @param {string} notebookData.interpreterGroup - Interpreter group to use
   * @returns {Promise<Object>} Created notebook data
   */
  async create(notebookData) {
    const response = await api.post('/zeppelin/add', notebookData)
    return response.data
  },

  /**
   * Get available interpreter schemas
   * @returns {Promise<Array>} List of active interpreter schemas
   */
  async getInterpreters() {
    const response = await api.get('/schema/active/')
    // Filter for interpreter schemas
    return response.data.filter(schema => schema.schemaName === 'interpreter')
  },

  /**
   * Get Zeppelin notebook link URL
   * @param {Object} notebook - Notebook object
   * @returns {string} Zeppelin UI URL for the notebook
   */
  getNotebookLink(notebook) {
    if (!notebook || !notebook.zeppelinNoteId) {
      return null
    }
    const baseUrl = process.env.VUE_APP_ZEPPELIN_UI_URL || 'http://localhost:8082'
    return `${baseUrl}/#/notebook/${notebook.zeppelinNoteId}`
  }
}

export default zeppelinService
