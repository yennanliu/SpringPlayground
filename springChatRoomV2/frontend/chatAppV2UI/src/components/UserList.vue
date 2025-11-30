<template>
  <div class="user-list">
    <div class="user-list-header">
      <h3>Online Users</h3>
      <span class="user-count">{{ onlineUsers.length }}</span>
    </div>

    <div class="users-section">
      <div
        v-for="user in onlineUsers"
        :key="user.id"
        class="user-item"
        @click="handleUserClick(user)"
        :title="`View ${user.username}'s profile`"
      >
        <Avatar
          :username="user.username"
          :avatar-url="user.avatarUrl"
          :show-online="true"
          :is-online="user.isOnline"
          size="medium"
        />
        <div class="user-info">
          <span class="user-name">{{ user.username }}</span>
          <span v-if="user.id === currentUserId" class="you-badge">(you)</span>
        </div>
      </div>

      <div v-if="onlineUsers.length === 0" class="empty-state">
        <p>No users online</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useChannelsStore } from '../stores/channels'
import Avatar from './Avatar.vue'

const userStore = useUserStore()
const channelsStore = useChannelsStore()

const emit = defineEmits(['show-profile'])

// Mock online users - In real implementation, this would come from WebSocket
const onlineUsers = ref([])

const currentUserId = computed(() => userStore.userId)

function handleUserClick(user) {
  emit('show-profile', user)
}

// Mock function to simulate online users
function loadOnlineUsers() {
  // In real implementation, this would be fetched from backend
  // For now, add current user
  onlineUsers.value = [
    {
      id: userStore.userId,
      username: userStore.username,
      email: userStore.currentUser?.email || '',
      displayName: userStore.currentUser?.displayName || '',
      avatarUrl: userStore.currentUser?.avatarUrl || null,
      statusMessage: userStore.currentUser?.statusMessage || '',
      isOnline: true
    }
  ]
}

onMounted(() => {
  loadOnlineUsers()
})
</script>

<style scoped>
.user-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #2c2f33;
  color: #dcddde;
  border-left: 1px solid #23272a;
}

.user-list-header {
  padding: 16px;
  border-bottom: 1px solid #23272a;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-list-header h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 700;
  color: white;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.user-count {
  padding: 4px 8px;
  background-color: #5865f2;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 700;
  color: white;
}

.users-section {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  margin: 2px 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.user-item:hover {
  background-color: #393c43;
}


.user-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 6px;
}

.user-name {
  font-size: 15px;
  font-weight: 500;
  color: #dcddde;
}

.you-badge {
  font-size: 11px;
  color: #96989d;
  font-weight: 600;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
  color: #72767d;
  font-size: 13px;
}

.empty-state p {
  margin: 0;
}

/* Scrollbar styling */
.users-section::-webkit-scrollbar {
  width: 6px;
}

.users-section::-webkit-scrollbar-track {
  background: transparent;
}

.users-section::-webkit-scrollbar-thumb {
  background: #1e2124;
  border-radius: 3px;
}

.users-section::-webkit-scrollbar-thumb:hover {
  background: #2c2f33;
}

/* Responsive */
@media (max-width: 1024px) {
  .user-list {
    width: 200px;
  }

  .user-list-header h3 {
    font-size: 12px;
  }

  .user-item {
    padding: 8px 12px;
  }

  .user-avatar {
    width: 28px;
    height: 28px;
  }

  .avatar-text {
    font-size: 11px;
  }
}
</style>
