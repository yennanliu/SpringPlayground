<template>
  <div class="dm-view">
    <header class="dm-header">
      <div class="user-info">
        <div class="user-avatar">
          <img :src="user?.avatar || '/default-avatar.png'" :alt="userName">
          <div class="status-indicator" :class="{ 'active': userStatus === 'active' }"></div>
        </div>
        <div class="user-details">
          <h2>{{ userName }}</h2>
          <div class="user-status">
            {{ userStatus === 'active' ? 'Active' : 'Away' }}
          </div>
        </div>
      </div>
      
      <div class="dm-actions">
        <div class="search-container">
          <input 
            type="text" 
            placeholder="Search in conversation" 
            v-model="searchQuery"
            @keyup.enter="searchMessages"
          >
          <i class="fas fa-search"></i>
        </div>
        
        <button class="action-button" @click="showUserProfile = true">
          <i class="fas fa-user"></i>
          Profile
        </button>
        
        <button class="action-button">
          <i class="fas fa-phone"></i>
          Call
        </button>
      </div>
    </header>
    
    <ChatArea :channelId="'dm_' + userId" />
    
    <!-- User Profile Modal -->
    <div v-if="showUserProfile" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>User Profile</h3>
          <button @click="showUserProfile = false" class="close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="profile-container">
            <div class="profile-header">
              <img :src="user?.avatar || '/default-avatar.png'" :alt="userName" class="profile-avatar">
              <h2>{{ userName }}</h2>
              <div class="profile-status" :class="{ 'active': userStatus === 'active' }">
                {{ userStatus === 'active' ? 'Active' : 'Away' }}
              </div>
              <p class="profile-title">{{ user?.title || 'No title set' }}</p>
            </div>
            
            <div class="profile-section">
              <h4>Contact Information</h4>
              <div class="profile-field">
                <i class="fas fa-envelope"></i>
                <span>{{ user?.email || 'No email available' }}</span>
              </div>
              <div class="profile-field">
                <i class="fas fa-phone"></i>
                <span>{{ user?.phone || 'No phone number available' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { apiGetUser } from '@/services/api';
import ChatArea from '@/components/ChatArea.vue';

const route = useRoute();
const userId = computed(() => route.params.userId);
const user = ref(null);
const userStatus = ref('active'); // Default status
const searchQuery = ref('');
const showUserProfile = ref(false);

const userName = computed(() => 
  user.value?.displayName || 'User'
);

const searchMessages = () => {
  if (searchQuery.value.trim()) {
    // Implement message search functionality
    console.log('Searching for:', searchQuery.value);
  }
};

const fetchUserData = async () => {
  try {
    user.value = await apiGetUser(userId.value);
  } catch (error) {
    console.error('Failed to fetch user:', error);
  }
};

// Fetch user data when userId changes
watch(() => userId.value, () => {
  fetchUserData();
}, { immediate: true });

onMounted(() => {
  fetchUserData();
});
</script>

<style scoped>
.dm-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.dm-header {
  padding: 12px 20px;
  border-bottom: 1px solid #e2e2e2;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  position: relative;
  margin-right: 12px;
}

.user-avatar img {
  width: 36px;
  height: 36px;
  border-radius: 4px;
}

.status-indicator {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #ccc;
  border: 2px solid white;
}

.status-indicator.active {
  background-color: var(--slack-green);
}

.user-details h2 {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 4px 0;
}

.user-status {
  font-size: 14px;
  color: #616061;
}

.dm-actions {
  display: flex;
  align-items: center;
}

.search-container {
  position: relative;
  margin-right: 12px;
}

.search-container input {
  padding: 8px 12px 8px 32px;
  border: 1px solid #e2e2e2;
  border-radius: 4px;
  font-size: 14px;
  width: 200px;
}

.search-container i {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #616061;
}

.action-button {
  background: none;
  border: none;
  padding: 6px 10px;
  margin-left: 8px;
  cursor: pointer;
  color: #616061;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.action-button i {
  margin-right: 5px;
}

.action-button:hover {
  color: var(--slack-blue);
}

/* Modal styles - similar to ChannelView */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 95%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e2e2e2;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
}

.close-button {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  color: #616061;
}

.modal-body {
  padding: 20px;
}

.profile-container {
  display: flex;
  flex-direction: column;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.profile-avatar {
  width: 72px;
  height: 72px;
  border-radius: 4px;
  margin-bottom: 12px;
}

.profile-header h2 {
  margin: 4px 0;
  font-size: 20px;
}

.profile-status {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  background-color: #f0f0f0;
  margin-bottom: 4px;
}

.profile-status.active {
  background-color: #e8f5ea;
  color: var(--slack-green);
}

.profile-title {
  color: #616061;
  margin: 4px 0;
}

.profile-section {
  margin-top: 16px;
}

.profile-section h4 {
  margin-top: 0;
  margin-bottom: 12px;
  color: #1d1c1d;
  font-size: 16px;
}

.profile-field {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.profile-field i {
  width: 20px;
  color: #616061;
  margin-right: 8px;
}
</style> 