import api from './api'
import type { Jar } from '@/types'

/**
 * Service for JAR file management operations
 */
const jarService = {
  /**
   * Get all JAR files
   */
  async getAll(): Promise<Jar[]> {
    const response = await api.get<Jar[]>('/jar/')
    return response.data
  },

  /**
   * Get a JAR file by ID
   */
  async getById(id: number | string): Promise<Jar> {
    const response = await api.get<Jar>(`/jar/${id}`)
    return response.data
  },

  /**
   * Upload a new JAR file
   */
  async create(file: File): Promise<Jar> {
    const formData = new FormData()
    formData.append('jarfile', file)

    const response = await api.post<Jar>('/jar/add_jar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  /**
   * Delete a JAR file by ID
   */
  async delete(id: number | string): Promise<void> {
    await api.delete(`/jar/${id}`)
  }
}

export default jarService
