import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '@/stores'
import { authService } from '@/services'

jest.mock('@/services', () => ({
  authService: {
    signin: jest.fn(),
    signup: jest.fn(),
    signout: jest.fn(),
    setToken: jest.fn(),
    getToken: jest.fn()
  }
}))

describe('auth.store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    jest.clearAllMocks()
    localStorage.getItem.mockReturnValue(null)
  })

  describe('initial state', () => {
    it('should have null token initially', () => {
      const store = useAuthStore()
      expect(store.token).toBeNull()
    })

    it('should not be authenticated initially', () => {
      const store = useAuthStore()
      expect(store.isAuthenticated).toBe(false)
    })
  })

  describe('signin action', () => {
    it('should set token on successful signin', async () => {
      authService.signin.mockResolvedValue({ token: 'test-token' })

      const store = useAuthStore()
      await store.signin('test@example.com', 'password')

      expect(store.token).toBe('test-token')
      expect(authService.setToken).toHaveBeenCalledWith('test-token')
    })

    it('should set loading state during signin', async () => {
      authService.signin.mockImplementation(() =>
        new Promise(resolve => setTimeout(() => resolve({ token: 'token' }), 100))
      )

      const store = useAuthStore()
      const promise = store.signin('test@example.com', 'password')

      expect(store.loading).toBe(true)

      await promise
      expect(store.loading).toBe(false)
    })

    it('should set error on failed signin', async () => {
      const error = new Error('Invalid credentials')
      error.response = { data: { message: 'Invalid credentials' } }
      authService.signin.mockRejectedValue(error)

      const store = useAuthStore()

      await expect(store.signin('test@example.com', 'wrong'))
        .rejects
        .toThrow()

      expect(store.error).toBe('Invalid credentials')
    })
  })

  describe('signout action', () => {
    it('should clear token and user', () => {
      const store = useAuthStore()
      store.token = 'some-token'
      store.user = { name: 'Test' }

      store.signout()

      expect(store.token).toBeNull()
      expect(store.user).toBeNull()
      expect(authService.signout).toHaveBeenCalled()
    })
  })

  describe('checkAuth action', () => {
    it('should return true if token exists', () => {
      authService.getToken.mockReturnValue('existing-token')

      const store = useAuthStore()
      const result = store.checkAuth()

      expect(result).toBe(true)
      expect(store.token).toBe('existing-token')
    })

    it('should return false if no token', () => {
      authService.getToken.mockReturnValue(null)

      const store = useAuthStore()
      const result = store.checkAuth()

      expect(result).toBe(false)
    })
  })

  describe('isAuthenticated getter', () => {
    it('should return true when token is set', () => {
      const store = useAuthStore()
      store.token = 'valid-token'

      expect(store.isAuthenticated).toBe(true)
    })

    it('should return false when token is null', () => {
      const store = useAuthStore()
      store.token = null

      expect(store.isAuthenticated).toBe(false)
    })
  })
})
