<template>
  <aside class="sidebar">
    <div class="workspace-header">
      <h1>{{ workspaceName }}</h1>
      <div class="user-info" @click="toggleUserMenu">
        <div class="user-status" :class="{ 'active': userStore.currentUser?.status === 'active' }"></div>
        <span>{{ userStore.currentUser?.displayName }}</span>
        <i class="fas fa-chevron-down"></i>
      </div>
      <div v-if="showUserMenu" class="user-menu">
        <ul>
          <li @click="setStatus('active')">Set as Active</li>
          <li @click="setStatus('away')">Set as Away</li>
          <li @click="openProfile">View Profile</li>
          <li @click="logout">Sign Out</li>
        </ul>
      </div>
    </div>
    
    <div class="sidebar-section">
      <div class="section-header" @click="toggleThreads">
        <i class="fas" :class="showThreads ? 'fa-chevron-down' : 'fa-chevron-right'"></i>
        <span>Threads</span>
      </div>
      <div v-if="showThreads" class="section-content threads">
        <div class="thread-item">
          <!-- Thread items would go here -->
        </div>
      </div>
    </div>
    
    <div class="sidebar-section">
      <div class="section-header" @click="toggleChannels">
        <i class="fas" :class="showChannels ? 'fa-chevron-down' : 'fa-chevron-right'"></i>
        <span>Channels</span>
        <button class="add-button" @click.stop="openCreateChannelModal">
          <i class="fas fa-plus"></i>
        </button>
      </div>
      <div v-if="showChannels" class="section-content">
        <router-link 
          v-for="channel in channelStore.publicChannels" 
          :key="channel.id"
          :to="`/channels/${channel.id}`"
          class="sidebar-item"
          :class="{ active: currentRoute.params.channelId === channel.id }"
        >
          <span class="prefix">#</span>
          <span>{{ channel.name }}</span>
        </router-link>
      </div>
    </div>
    
    <div class="sidebar-section">
      <div class="section-header" @click="toggleDirectMessages">
        <i class="fas" :class="showDirectMessages ? 'fa-chevron-down' : 'fa-chevron-right'"></i>
        <span>Direct Messages</span>
        <button class="add-button" @click.stop="openNewDMModal">
          <i class="fas fa-plus"></i>
        </button>
      </div>
      <div v-if="showDirectMessages" class="section-content">
        <router-link 
          v-for="dm in directMessages" 
          :key="dm.userId"
          :to="`/dm/${dm.userId}`"
          class="sidebar-item"
          :class="{ active: currentRoute.path === `/dm/${dm.userId}` }"
        >
          <span class="status-circle" :class="dm.isActive ? 'active' : 'away'"></span>
          <span>{{ dm.displayName }}</span>
        </router-link>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '@/stores/userStore';
import { useChannelStore } from '@/stores/channelStore';

const userStore = useUserStore();
const channelStore = useChannelStore();
const router = useRouter();
const currentRoute = useRoute();

const workspaceName = ref('Slack Clone');
const showUserMenu = ref(false);
const showThreads = ref(false);
const showChannels = ref(true);
const showDirectMessages = ref(true);

// Mock data - in a real app you'd fetch this from API
const directMessages = ref([
  { userId: '1', displayName: 'Jane Doe', isActive: true },
  { userId: '2', displayName: 'John Smith', isActive: false },
]);

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

const toggleThreads = () => {
  showThreads.value = !showThreads.value;
};

const toggleChannels = () => {
  showChannels.value = !showChannels.value;
};

const toggleDirectMessages = () => {
  showDirectMessages.value = !showDirectMessages.value;
};

const setStatus = (status) => {
  // API call to update user status
  showUserMenu.value = false;
};

const openProfile = () => {
  // Open profile modal
  showUserMenu.value = false;
};

const openCreateChannelModal = () => {
  // Open create channel modal
};

const openNewDMModal = () => {
  // Open new DM modal
};

const logout = async () => {
  await userStore.logout();
  router.push('/login');
};

onMounted(async () => {
  await channelStore.fetchChannels();
});
</script>

<style scoped>
.sidebar {
  width: 260px;
  background-color: var(--slack-sidebar);
  color: var(--slack-text);
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.workspace-header {
  padding: 16px;
  border-bottom: 1px solid var(--slack-border);
  position: relative;
}

.workspace-header h1 {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px 0;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 0;
}

.user-status {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #ccc;
  margin-right: 8px;
}

.user-status.active {
  background-color: var(--slack-green);
}

.user-menu {
  position: absolute;
  top: 100%;
  left: 16px;
  background-color: #222;
  border-radius: 6px;
  box-shadow: 0 0 8px rgba(0,0,0,0.3);
  z-index: 10;
  min-width: 200px;
}

.user-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-menu li {
  padding: 8px 16px;
  cursor: pointer;
}

.user-menu li:hover {
  background-color: #333;
}

.sidebar-section {
  margin-top: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  padding: 0 16px;
  cursor: pointer;
  font-weight: 500;
}

.section-header i {
  margin-right: 6px;
  font-size: 12px;
}

.section-header .add-button {
  margin-left: auto;
  background: none;
  border: none;
  color: var(--slack-text);
  cursor: pointer;
  padding: 4px;
}

.section-content {
  margin-top: 6px;
}

.sidebar-item {
  display: flex;
  align-items: center;
  padding: 4px 16px;
  color: var(--slack-text);
  text-decoration: none;
  margin: 2px 0;
}

.sidebar-item:hover {
  background-color: rgba(255,255,255,0.1);
}

.sidebar-item.active {
  background-color: var(--slack-blue);
  color: white;
}

.prefix {
  margin-right: 6px;
  font-weight: 700;
}

.status-circle {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}

.status-circle.active {
  background-color: var(--slack-green);
}

.status-circle.away {
  background-color: #ccc;
}
</style> 