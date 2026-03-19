// Global test setup for Vue 3

// Create localStorage mock functions
const getItemMock = jest.fn()
const setItemMock = jest.fn()
const removeItemMock = jest.fn()
const clearMock = jest.fn()

// Mock localStorage
const localStorageMock = {
  store: {},
  getItem: getItemMock,
  setItem: setItemMock,
  removeItem: removeItemMock,
  clear: clearMock
}

Object.defineProperty(global, 'localStorage', {
  value: localStorageMock,
  writable: true
})

// Reset localStorage mock before each test
beforeEach(() => {
  localStorageMock.store = {}
  getItemMock.mockImplementation((key) => localStorageMock.store[key] || null)
  setItemMock.mockImplementation((key, value) => {
    localStorageMock.store[key] = value
  })
  removeItemMock.mockImplementation((key) => {
    delete localStorageMock.store[key]
  })
  clearMock.mockImplementation(() => {
    localStorageMock.store = {}
  })
})
