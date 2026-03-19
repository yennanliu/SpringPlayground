import api from './api'
import type { Notebook, NotebookCreateData } from '@/types'

interface Schema {
  schemaName: string
  [key: string]: unknown
}

/**
 * Service for Zeppelin notebook operations
 */
const zeppelinService = {
  /**
   * Get all notebooks
   */
  async getAll(): Promise<Notebook[]> {
    const response = await api.get<Notebook[]>('/zeppelin/')
    return response.data
  },

  /**
   * Get a notebook by ID
   */
  async getById(id: string | number): Promise<Notebook> {
    const response = await api.get<Notebook>(`/zeppelin/${id}`)
    return response.data
  },

  /**
   * Create a new notebook
   */
  async create(notebookData: NotebookCreateData): Promise<Notebook> {
    const response = await api.post<Notebook>('/zeppelin/add', notebookData)
    return response.data
  },

  /**
   * Get available interpreter schemas
   */
  async getInterpreters(): Promise<Schema[]> {
    const response = await api.get<Schema[]>('/schema/active/')
    // Filter for interpreter schemas
    return response.data.filter(schema => schema.schemaName === 'interpreter')
  },

  /**
   * Get Zeppelin notebook link URL
   */
  getNotebookLink(notebook: Notebook & { zeppelinNoteId?: string }): string | null {
    if (!notebook || !notebook.zeppelinNoteId) {
      return null
    }
    const baseUrl = process.env.VUE_APP_ZEPPELIN_UI_URL || 'http://localhost:8082'
    return `${baseUrl}/#/notebook/${notebook.zeppelinNoteId}`
  }
}

export default zeppelinService
