import api from './api'

/**
 * Service for Flink cluster management operations
 */
const clusterService = {
  /**
   * Get all clusters
   * @returns {Promise<Array>} List of clusters
   */
  async getAll() {
    const response = await api.get('/cluster/')
    return response.data
  },

  /**
   * Get a cluster by ID
   * @param {string|number} id - Cluster ID
   * @returns {Promise<Object>} Cluster data
   */
  async getById(id) {
    const response = await api.get(`/cluster/${id}`)
    return response.data
  },

  /**
   * Add a new cluster
   * @param {Object} clusterData - Cluster configuration
   * @param {string} clusterData.url - Cluster URL
   * @param {string|number} clusterData.port - Cluster port
   * @returns {Promise<Object>} Created cluster data
   */
  async create(clusterData) {
    const response = await api.post('/cluster/add_cluster', clusterData)
    return response.data
  },

  /**
   * Delete a cluster by ID
   * @param {string|number} id - Cluster ID
   * @returns {Promise<Object>} Delete response
   */
  async delete(id) {
    const response = await api.delete(`/cluster/${id}`)
    return response.data
  }
}

export default clusterService
