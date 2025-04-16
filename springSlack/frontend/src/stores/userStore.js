import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { apiLogin, apiCheckAuth, apiLogout } from '@/services/api';

export const useUserStore = defineStore('user', () => {
  const currentUser = ref(null);
  const isLoading = ref(false);
  const error = ref(null);

  const isAuthenticated = computed(() => !!currentUser.value);

  async function login(credentials) {
    isLoading.value = true;
    error.value = null;
    try {
      const userData = await apiLogin(credentials);
      currentUser.value = userData;
      return userData;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      isLoading.value = false;
    }
  }

  async function logout() {
    isLoading.value = true;
    try {
      await apiLogout();
      currentUser.value = null;
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  async function checkAuth() {
    isLoading.value = true;
    try {
      const userData = await apiCheckAuth();
      currentUser.value = userData;
      return userData;
    } catch (err) {
      currentUser.value = null;
    } finally {
      isLoading.value = false;
    }
  }

  return {
    currentUser,
    isLoading,
    error,
    isAuthenticated,
    login,
    logout,
    checkAuth
  };
}); 