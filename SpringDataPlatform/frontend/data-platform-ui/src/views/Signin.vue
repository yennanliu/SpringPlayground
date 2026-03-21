<template>
  <div class="auth-page">
    <div class="auth-container">
      <!-- Logo -->
      <div class="auth-logo">
        <router-link :to="{ name: 'Home' }">
          <img id="logo" src="../assets/icon3.png" alt="Logo" />
        </router-link>
      </div>

      <!-- Auth Card -->
      <div class="auth-card">
        <h2 class="auth-title">Sign In</h2>
        <p class="auth-subtitle">Welcome back! Please enter your credentials</p>

        <Form @submit="signin" class="auth-form" v-slot="{ meta }">
          <div class="form-group">
            <label for="email">Email</label>
            <div class="input-wrapper">
              <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              <Field
                name="email"
                type="email"
                class="form-control"
                :class="{ 'is-invalid': errors.email }"
                v-model="email"
                rules="required|email"
                placeholder="you@example.com"
              />
            </div>
            <ErrorMessage name="email" class="invalid-feedback" />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="input-wrapper">
              <svg class="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              <Field
                name="password"
                type="password"
                class="form-control"
                :class="{ 'is-invalid': errors.password }"
                v-model="password"
                rules="required|min:6"
                placeholder="Enter your password"
              />
            </div>
            <ErrorMessage name="password" class="invalid-feedback" />
          </div>

          <p class="form-text">
            By continuing, you agree to our Terms of Service and Privacy Policy.
          </p>

          <button type="submit" class="btn-submit" :disabled="!meta.valid || loading">
            <span v-if="!loading">Sign In</span>
            <span v-else class="btn-loading">
              <div class="spinner-border spinner-border-sm" role="status">
                <span class="sr-only">Loading...</span>
              </div>
              Signing in...
            </span>
          </button>
        </Form>

        <div class="auth-divider">
          <span>or</span>
        </div>

        <div class="auth-footer">
          <p>New to the platform?</p>
          <router-link :to="{ name: 'Signup' }" class="btn-secondary">
            Create Account
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate'
import swal from "sweetalert"
import { useAuthStore } from "@/stores"

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const errors = ref({})

const loading = computed(() => authStore.loading)

const signin = async () => {
  try {
    await authStore.signin(email.value, password.value)
    const redirect = route.query.redirect || "/"
    router.push(redirect)
  } catch (error) {
    swal({
      text: "Unable to Log you in!",
      icon: "error",
      closeOnClickOutside: false,
    })
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 40px 20px;
}

.auth-container {
  width: 100%;
  max-width: 420px;
}

.auth-logo {
  text-align: center;
  margin-bottom: 32px;
}

#logo {
  width: 120px;
  transition: transform 0.3s ease;
}

#logo:hover {
  transform: scale(1.05);
}

.auth-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  padding: 40px;
}

.auth-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #212529;
  margin: 0 0 8px;
  text-align: center;
}

.auth-subtitle {
  color: #6c757d;
  text-align: center;
  margin: 0 0 32px;
  font-size: 0.95rem;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-weight: 500;
  color: #495057;
  font-size: 0.9rem;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #adb5bd;
  pointer-events: none;
  transition: color 0.2s ease;
}

.form-control {
  width: 100%;
  padding: 14px 14px 14px 46px;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.2s ease;
  background-color: #fafafa;
}

.form-control:focus {
  outline: none;
  border-color: #f0c14b;
  background-color: #ffffff;
  box-shadow: 0 0 0 4px rgba(240, 193, 75, 0.15);
}

.form-control:focus + .input-icon,
.input-wrapper:focus-within .input-icon {
  color: #f0c14b;
}

.form-control::placeholder {
  color: #adb5bd;
}

.form-control.is-invalid {
  border-color: #dc3545;
}

.invalid-feedback {
  color: #dc3545;
  font-size: 0.85rem;
  margin-top: 4px;
}

.form-text {
  color: #6c757d;
  font-size: 0.8rem;
  text-align: center;
  margin: 0;
}

.btn-submit {
  width: 100%;
  padding: 14px 24px;
  background-color: #f0c14b;
  color: #000000;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
  margin-top: 8px;
}

.btn-submit:hover:not(:disabled) {
  background-color: #e0b03b;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(240, 193, 75, 0.35);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-loading {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.auth-divider {
  display: flex;
  align-items: center;
  margin: 28px 0;
}

.auth-divider::before,
.auth-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background-color: #e9ecef;
}

.auth-divider span {
  padding: 0 16px;
  color: #adb5bd;
  font-size: 0.85rem;
}

.auth-footer {
  text-align: center;
}

.auth-footer p {
  color: #6c757d;
  margin: 0 0 12px;
  font-size: 0.9rem;
}

.btn-secondary {
  display: inline-block;
  width: 100%;
  padding: 12px 24px;
  background-color: #f8f9fa;
  color: #212529;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 0.95rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background-color: #e9ecef;
  border-color: #dee2e6;
}

@media (max-width: 480px) {
  .auth-card {
    padding: 30px 24px;
  }
}
</style>
