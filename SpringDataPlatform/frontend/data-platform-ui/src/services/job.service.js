import api from './api'

/**
 * Service for Flink job management operations
 */
const jobService = {
  /**
   * Get all jobs
   * @returns {Promise<Array>} List of jobs
   */
  async getAll() {
    const response = await api.get('/job/')
    return response.data
  },

  /**
   * Get a job by ID
   * @param {string|number} id - Job ID
   * @returns {Promise<Object>} Job data
   */
  async getById(id) {
    const response = await api.get(`/job/${id}`)
    return response.data
  },

  /**
   * Submit a new JAR-based job
   * @param {Object} jobData - Job configuration
   * @param {string|number} jobData.jarId - JAR file ID to run
   * @param {number} [jobData.parallelism=1] - Job parallelism
   * @param {string} [jobData.entryClass] - Entry class name
   * @param {string} [jobData.programArgs] - Program arguments
   * @param {string} [jobData.savePointPath] - Savepoint path
   * @param {boolean} [jobData.allowNonRestoredState] - Allow non-restored state
   * @returns {Promise<Object>} Created job data
   */
  async create(jobData) {
    const payload = {
      jarId: jobData.jarId,
      parallelism: jobData.parallelism || 1,
      entryClass: jobData.entryClass || null,
      programArgs: jobData.programArgs || null,
      savePointPath: jobData.savePointPath || null,
      allowNonRestoredState: jobData.allowNonRestoredState || null
    }

    const response = await api.post('/job/add', payload)
    return response.data
  },

  /**
   * Submit a new SQL job
   * @param {string} statement - SQL statement to execute
   * @returns {Promise<Object>} Created job data
   */
  async createSqlJob(statement) {
    const response = await api.post('/sql_job/add', { statement })
    return response.data
  },

  /**
   * Get Flink job link URL
   * @param {Object} job - Job object
   * @returns {string} Flink UI URL for the job
   */
  getFlinkJobLink(job) {
    if (!job || !job.jobId) {
      return null
    }
    const baseUrl = process.env.VUE_APP_FLINK_UI_URL || 'http://localhost:8081'
    const state = (job.state || '').toLowerCase()
    return `${baseUrl}/#/job/${state}/${job.jobId}`
  }
}

export default jobService
