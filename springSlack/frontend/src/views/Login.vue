<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <img src="/slack-logo.svg" alt="Slack Logo" class="logo">
        <h1>Sign in to Slack</h1>
      </div>
      
      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="error-message" v-if="error">
          {{ error }}
        </div>
        
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            required 
            autocomplete="email"
          >
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            required
            autocomplete="current-password"
          >
        </div>
        
        <button 
          type="submit" 
          class="auth-button" 
          :disabled="isLoading"
        >
          <span v-if="isLoading">
            <i class="fas fa-circle-notch fa-spin"></i> Signing in...
          </span>
          <span v-else>Sign In</span>
        </button>
      </form>
      
      <div class="auth-footer">
        <p>New to Slack? <router-link to="/register">Create an account</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const email = ref('');
const password = ref('');
const error = ref('');
const isLoading = computed(() => userStore.isLoading);

const handleLogin = async () => {
  error.value = '';
  
  try {
    await userStore.login({
      email: email.value,
      password: password.value
    });
    router.push('/');
  } catch (err) {
    error.value = err.message || 'Failed to sign in. Please try again.';
  }
};
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f8f8f8;
  padding: 20px;
}

.auth-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 480px;
  padding: 32px;
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  width: 80px;
  margin-bottom: 16px;
}

.auth-header h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.auth-form {
  margin-bottom: 24px;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 16px;
  font-size: 14px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-group input:focus {
  border-color: var(--slack-blue);
  outline: none;
  box-shadow: 0 0 0 1px var(--slack-blue);
}

.auth-button {
  width: 100%;
  padding: 12px;
  background-color: var(--slack-purple);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.auth-button:hover:not(:disabled) {
  background-color: #3f1340;
}

.auth-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.auth-footer {
  text-align: center;
  color: #666;
  font-size: 14px;
}

.auth-footer a {
  color: var(--slack-blue);
  text-decoration: none;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style> 