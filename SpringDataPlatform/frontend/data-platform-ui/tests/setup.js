// Global test setup
import Vue from 'vue'
import { createPinia, PiniaVuePlugin } from 'pinia'

// Enable Pinia
Vue.use(PiniaVuePlugin)

// Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn()
}
global.localStorage = localStorageMock

// Suppress Vue warnings in tests
Vue.config.silent = true
Vue.config.productionTip = false
