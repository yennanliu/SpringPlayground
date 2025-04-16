<template>
  <div class="app-container">
    <Sidebar v-if="isAuthenticated" />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/userStore';
import Sidebar from '@/components/Sidebar.vue';

const userStore = useUserStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);

onMounted(async () => {
  await userStore.checkAuth();
});
</script>

<style>
.app-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style> 