import api from './api'
import type { Job, SqlJob, JobCreateData, SqlJobCreateData } from '@/types'

/**
 * Service for Flink job management operations
 */
const jobService = {
  /**
   * Get all jobs
   */
  async getAll(): Promise<Job[]> {
    const response = await api.get<Job[]>('/job/')
    return response.data
  },

  /**
   * Get a job by ID
   */
  async getById(id: number | string): Promise<Job> {
    const response = await api.get<Job>(`/job/${id}`)
    return response.data
  },

  /**
   * Submit a new JAR-based job
   */
  async create(jobData: JobCreateData): Promise<Job> {
    const payload = {
      jarId: jobData.jarId,
      parallelism: jobData.parallelism || 1,
      entryClass: jobData.entryClass || null,
      programArgs: jobData.programArgs || null,
      savePointPath: jobData.savePointPath || null,
      allowNonRestoredState: jobData.allowNonRestoredState || null
    }

    const response = await api.post<Job>('/job/add', payload)
    return response.data
  },

  /**
   * Get all SQL jobs
   */
  async getAllSqlJobs(): Promise<SqlJob[]> {
    const response = await api.get<SqlJob[]>('/sql_job')
    return response.data
  },

  /**
   * Submit a new SQL job
   */
  async createSqlJob(statement: string): Promise<SqlJob> {
    const data: SqlJobCreateData = { statement }
    const response = await api.post<SqlJob>('/sql_job', data)
    return response.data
  },

  /**
   * Get Flink job link URL
   */
  getFlinkJobLink(job: Job): string | null {
    if (!job || !job.jobId) {
      return null
    }
    const baseUrl = process.env.VUE_APP_FLINK_UI_URL || 'http://localhost:8081'
    const state = (job.state || '').toLowerCase()
    return `${baseUrl}/#/job/${state}/${job.jobId}`
  }
}

export default jobService
