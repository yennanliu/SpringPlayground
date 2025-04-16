<template>
  <div class="channel-view">
    <header class="channel-header">
      <div class="channel-info">
        <h2>
          <span class="channel-prefix">#</span>
          {{ channelName }}
        </h2>
        <button class="details-button" @click="showChannelDetails = true">
          <i class="fas fa-info-circle"></i>
          Details
        </button>
      </div>
      
      <div class="channel-actions">
        <div class="search-container">
          <input 
            type="text" 
            placeholder="Search in channel" 
            v-model="searchQuery"
            @keyup.enter="searchChannel"
          >
          <i class="fas fa-search"></i>
        </div>
        
        <button class="channel-action-button" @click="showMembersList = true">
          <i class="fas fa-user-friends"></i>
          <span>{{ memberCount }}</span>
        </button>
        
        <button class="channel-action-button" @click="showChannelSettings = true">
          <i class="fas fa-cog"></i>
        </button>
      </div>
    </header>
    
    <ChatArea :channelId="channelId" />
    
    <!-- Channel Details Modal -->
    <div v-if="showChannelDetails" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h3># {{ channelName }}</h3>
          <button @click="showChannelDetails = false" class="close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="channel-description">
            <h4>About this channel</h4>
            <p>{{ channelDescription || 'No description available' }}</p>
          </div>
          <div class="channel-created">
            <p>Created by {{ createdBy }} on {{ formatDate(createdAt) }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Members List Modal -->
    <div v-if="showMembersList" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Members</h3>
          <button @click="showMembersList = false" class="close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <ul class="members-list">
            <li v-for="member in members" :key="member.id" class="member-item">
              <img :src="member.avatar || '/default-avatar.png'" :alt="member.displayName">
              <span>{{ member.displayName }}</span>
              <span v-if="member.isAdmin" class="admin-badge">Admin</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { format } from 'date-fns';
import { useChannelStore } from '@/stores/channelStore';
import ChatArea from '@/components/ChatArea.vue';

const route = useRoute();
const channelStore = useChannelStore();

const searchQuery = ref('');
const showChannelDetails = ref(false);
const showMembersList = ref(false);
const showChannelSettings = ref(false);

const channelId = computed(() => route.params.channelId);

const channelName = computed(() => 
  channelStore.currentChannel?.name || 'unknown-channel'
);

const channelDescription = computed(() => 
  channelStore.currentChannel?.description || ''
);

const createdBy = computed(() => 
  channelStore.currentChannel?.createdBy?.displayName || 'Unknown'
);

const createdAt = computed(() => 
  channelStore.currentChannel?.createdAt || new Date()
);

const members = computed(() => 
  channelStore.currentChannel?.members || []
);

const memberCount = computed(() => 
  members.value.length
);

const formatDate = (date) => {
  return format(new Date(date), 'MMMM d, yyyy');
};

const searchChannel = () => {
  if (searchQuery.value.trim()) {
    // Implement channel search functionality
    console.log('Searching for:', searchQuery.value);
  }
};

watch(() => route.params.channelId, async (newChannelId) => {
  if (newChannelId) {
    await channelStore.fetchChannel(newChannelId);
  }
});

onMounted(async () => {
  if (channelId.value) {
    await channelStore.fetchChannel(channelId.value);
  }
});
</script>

<style scoped>
.channel-view {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.channel-header {
  padding: 12px 20px;
  border-bottom: 1px solid #e2e2e2;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.channel-info {
  display: flex;
  align-items: center;
}

.channel-info h2 {
  font-size: 18px;
  font-weight: 700;
  margin: 0;
}

.channel-prefix {
  color: #616061;
  margin-right: 4px;
}

.details-button {
  background: none;
  border: none;
  color: #616061;
  margin-left: 12px;
  padding: 4px 8px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.details-button i {
  margin-right: 6px;
}

.details-button:hover {
  color: var(--slack-blue);
}

.channel-actions {
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

.channel-action-button {
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

.channel-action-button i {
  margin-right: 5px;
}

.channel-action-button:hover {
  color: var(--slack-blue);
}

/* Modal styles */
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
  max-width: 600px;
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

.channel-description h4 {
  margin-top: 0;
  color: #1d1c1d;
}

.channel-created {
  margin-top: 20px;
  color: #616061;
  font-size: 14px;
}

.members-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.member-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-item img {
  width: 36px;
  height: 36px;
  border-radius: 4px;
  margin-right: 12px;
}

.admin-badge {
  background-color: #e8f5fa;
  color: var(--slack-blue);
  padding: 2px 6px;
  border-radius: 12px;
  font-size: 12px;
  margin-left: auto;
}
</style> 