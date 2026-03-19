import api from './api'
import type { Cluster, ClusterCreateData } from '@/types'

/**
 * Service for Flink cluster management operations
 */
const clusterService = {
  /**
   * Get all clusters
   */
  async getAll(): Promise<Cluster[]> {
    const response = await api.get<Cluster[]>('/cluster/')
    return response.data
  },

  /**
   * Get a cluster by ID
   */
  async getById(id: number | string): Promise<Cluster> {
    const response = await api.get<Cluster>(`/cluster/${id}`)
    return response.data
  },

  /**
   * Add a new cluster
   */
  async create(clusterData: ClusterCreateData): Promise<Cluster> {
    const response = await api.post<Cluster>('/cluster/add_cluster', clusterData)
    return response.data
  },

  /**
   * Delete a cluster by ID
   */
  async delete(id: number | string): Promise<void> {
    await api.delete(`/cluster/${id}`)
  }
}

export default clusterService
