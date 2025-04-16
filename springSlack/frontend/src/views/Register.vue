<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <img src="/slack-logo.svg" alt="Slack Logo" class="logo">
        <h1>Create your Slack account</h1>
      </div>
      
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="error-message" v-if="error">
          {{ error }}
        </div>
        
        <div class="form-group">
          <label for="displayName">Display Name</label>
          <input 
            type="text" 
            id="displayName" 
            v-model="displayName" 
            required
            autocomplete="name"
          >
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
            autocomplete="new-password"
            minlength="8"
          >
          <small class="form-hint">Must be at least 8 characters</small>
        </div>
        
        <div class="form-group">
          <label for="confirmPassword">Confirm Password</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="confirmPassword" 
            required
            autocomplete="new-password"
          >
        </div>
        
        <button 
          type="submit" 
          class="auth-button" 
          :disabled="isLoading || !isFormValid"
        >
          <span v-if="isLoading">
            <i class="fas fa-circle-notch fa-spin"></i> Creating account...
          </span>
          <span v-else>Create Account</span>
        </button>
      </form>
      
      <div class="auth-footer">
        <p>Already have an account? <router-link to="/login">Sign in</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { apiRegister } from '@/services/api';
import { useUserStore } from '@/stores/userStore';

const router = useRouter();
const userStore = useUserStore();

const displayName = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');
const error = ref('');
const isLoading = ref(false);

const isFormValid = computed(() => {
  return (
    displayName.value.trim() !== '' &&
    email.value.trim() !== '' &&
    password.value.length >= 8 &&
    password.value === confirmPassword.value
  );
});

const handleRegister = async () => {
  if (!isFormValid.value) {
    if (password.value !== confirmPassword.value) {
      error.value = 'Passwords do not match';
    } else if (password.value.length < 8) {
      error.value = 'Password must be at least 8 characters';
    }
    return;
  }
  
  isLoading.value = true;
  error.value = '';
  
  try {
    await apiRegister({
      displayName: displayName.value,
      email: email.value,
      password: password.value
    });
    
    // Login with the new credentials
    await userStore.login({
      email: email.value,
      password: password.value
    });
    
    router.push('/');
  } catch (err) {
    error.value = err.message || 'Failed to create account. Please try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
/* Reuse styles from Login.vue */
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

.form-hint {
  display: block;
  color: #777;
  margin-top: 4px;
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