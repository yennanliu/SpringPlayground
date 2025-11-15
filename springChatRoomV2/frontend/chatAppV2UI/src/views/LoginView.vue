<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="app-title">Chat App V2</h1>
      <h2 class="login-subtitle">Sign In</h2>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="Enter your username"
            required
            :disabled="isLoading"
            class="form-input"
          />
          <span v-if="errors.username" class="error-message">
            {{ errors.username }}
          </span>
        </div>

        <div class="form-group">
          <label for="email">Email (optional)</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="Enter your email"
            :disabled="isLoading"
            class="form-input"
          />
          <span v-if="errors.email" class="error-message">
            {{ errors.email }}
          </span>
        </div>

        <button
          type="submit"
          :disabled="isLoading || !isFormValid"
          class="login-button"
        >
          {{ isLoading ? 'Signing in...' : 'Sign In' }}
        </button>

        <p v-if="errorMessage" class="error-banner">
          {{ errorMessage }}
        </p>
      </form>

      <p class="info-text">
        Phase 1 MVP - Simple login with username
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const email = ref('')
const isLoading = ref(false)
const errorMessage = ref('')
const errors = ref({
  username: '',
  email: ''
})

const isFormValid = computed(() => {
  return username.value.trim().length >= 3
})

function validateForm() {
  errors.value = { username: '', email: '' }
  let isValid = true

  // Username validation
  if (!username.value.trim()) {
    errors.value.username = 'Username is required'
    isValid = false
  } else if (username.value.trim().length < 3) {
    errors.value.username = 'Username must be at least 3 characters'
    isValid = false
  } else if (username.value.trim().length > 20) {
    errors.value.username = 'Username must be less than 20 characters'
    isValid = false
  }

  // Email validation (optional)
  if (email.value.trim()) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(email.value)) {
      errors.value.email = 'Invalid email format'
      isValid = false
    }
  }

  return isValid
}

async function handleLogin() {
  if (!validateForm()) {
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    // Simulate API call delay
    await new Promise(resolve => setTimeout(resolve, 500))

    // Login user
    userStore.login({
      username: username.value.trim(),
      email: email.value.trim() || `${username.value.trim()}@example.com`
    })

    // Redirect to chat
    router.push('/chat')
  } catch (error) {
    console.error('Login error:', error)
    errorMessage.value = 'Login failed. Please try again.'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

.app-title {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 700;
  color: #333;
  text-align: center;
}

.login-subtitle {
  margin: 0 0 30px 0;
  font-size: 18px;
  font-weight: 400;
  color: #666;
  text-align: center;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.form-input {
  padding: 12px 16px;
  font-size: 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  transition: border-color 0.3s;
  font-family: inherit;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
}

.form-input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.error-message {
  font-size: 12px;
  color: #f44336;
}

.login-button {
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  color: white;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, opacity 0.3s;
  margin-top: 10px;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
}

.login-button:active:not(:disabled) {
  transform: translateY(0);
}

.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-banner {
  margin: 10px 0 0 0;
  padding: 12px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 6px;
  font-size: 14px;
  text-align: center;
}

.info-text {
  margin: 20px 0 0 0;
  font-size: 12px;
  color: #999;
  text-align: center;
}
</style>
