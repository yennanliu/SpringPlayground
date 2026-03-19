import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'

/**
 * Axios instance configured with base URL and interceptors
 */
const api: AxiosInstance = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8081',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * Request interceptor - adds auth token to requests
 */
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

/**
 * Response interceptor - handles common error cases
 */
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error: AxiosError) => {
    if (error.response) {
      // Server responded with error status
      const { status } = error.response

      if (status === 401) {
        // Unauthorized - clear token and redirect to login
        localStorage.removeItem('token')
        // Redirect will be handled by router guard
      }

      if (status === 403) {
        // Forbidden - user doesn't have permission
      }

      if (status >= 500) {
        // Server error
      }
    } else if (error.request) {
      // Request made but no response received (network error)
    }

    return Promise.reject(error)
  }
)

export default api
