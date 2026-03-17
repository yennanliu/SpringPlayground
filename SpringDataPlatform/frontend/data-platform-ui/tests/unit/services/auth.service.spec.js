import authService from '@/services/auth.service'
import api from '@/services/api'

jest.mock('@/services/api')

describe('authService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.getItem.mockReturnValue(null)
    localStorage.setItem.mockClear()
    localStorage.removeItem.mockClear()
  })

  describe('signin', () => {
    it('should call API with correct credentials', async () => {
      const mockResponse = { data: { token: 'test-token' } }
      api.post.mockResolvedValue(mockResponse)

      const result = await authService.signin('test@example.com', 'password123')

      expect(api.post).toHaveBeenCalledWith('/users/signIn', {
        email: 'test@example.com',
        password: 'password123'
      })
      expect(result).toEqual({ token: 'test-token' })
    })

    it('should throw error when API fails', async () => {
      api.post.mockRejectedValue(new Error('Network error'))

      await expect(authService.signin('test@example.com', 'password123'))
        .rejects
        .toThrow('Network error')
    })
  })

  describe('signup', () => {
    it('should call API with user data', async () => {
      const mockResponse = { data: { message: 'User created' } }
      api.post.mockResolvedValue(mockResponse)

      const userData = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      }

      const result = await authService.signup(userData)

      expect(api.post).toHaveBeenCalledWith('/users/signup', userData)
      expect(result).toEqual({ message: 'User created' })
    })
  })

  describe('signout', () => {
    it('should remove token from localStorage', () => {
      authService.signout()

      expect(localStorage.removeItem).toHaveBeenCalledWith('token')
    })
  })

  describe('isAuthenticated', () => {
    it('should return true when token exists', () => {
      localStorage.getItem.mockReturnValue('some-token')

      expect(authService.isAuthenticated()).toBe(true)
    })

    it('should return false when token does not exist', () => {
      localStorage.getItem.mockReturnValue(null)

      expect(authService.isAuthenticated()).toBe(false)
    })
  })

  describe('setToken', () => {
    it('should store token in localStorage', () => {
      authService.setToken('new-token')

      expect(localStorage.setItem).toHaveBeenCalledWith('token', 'new-token')
    })
  })

  describe('getToken', () => {
    it('should return token from localStorage', () => {
      localStorage.getItem.mockReturnValue('stored-token')

      expect(authService.getToken()).toBe('stored-token')
    })
  })
})
