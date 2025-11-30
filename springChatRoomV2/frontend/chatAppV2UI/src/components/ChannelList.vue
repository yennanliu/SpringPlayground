<template>
  <div class="channel-list">
    <div class="channel-list-header">
      <h3>Channels</h3>
      <button
        class="add-channel-button"
        @click="showCreateModal = true"
        title="Create new channel"
      >
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"></line>
          <line x1="5" y1="12" x2="19" y2="12"></line>
        </svg>
      </button>
    </div>

    <div class="channels-section">
      <!-- Group Channels -->
      <div class="section-title">
        <span class="section-icon">#</span>
        <span>Group Channels</span>
      </div>

      <div
        v-for="channel in groupChannels"
        :key="channel.id"
        class="channel-item"
        :class="{ active: isActiveChannel(channel.id) }"
        @click="selectChannel(channel.id)"
      >
        <span class="channel-hash">#</span>
        <span class="channel-name">{{ channel.name }}</span>
        <span v-if="channel.unreadCount > 0" class="unread-badge">
          {{ channel.unreadCount }}
        </span>
      </div>

      <!-- Direct Messages -->
      <div v-if="directChannels.length > 0" class="section-title">
        <span class="section-icon">@</span>
        <span>Direct Messages</span>
      </div>

      <div
        v-for="channel in directChannels"
        :key="channel.id"
        class="channel-item"
        :class="{ active: isActiveChannel(channel.id) }"
        @click="selectChannel(channel.id)"
      >
        <div class="dm-avatar">
          <span class="avatar-text">{{ getDMInitials(channel.name) }}</span>
        </div>
        <span class="channel-name">{{ channel.name }}</span>
        <span v-if="channel.unreadCount > 0" class="unread-badge">
          {{ channel.unreadCount }}
        </span>
      </div>
    </div>

    <div class="info-section">
      <p class="info-text">
        Phase 2: Multi-channel & DM support
      </p>
    </div>

    <!-- Create Channel Modal -->
    <CreateChannelModal
      :is-open="showCreateModal"
      @close="showCreateModal = false"
      @created="handleChannelCreated"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useChannelsStore } from '../stores/channels'
import { useUserStore } from '../stores/user'
import CreateChannelModal from './CreateChannelModal.vue'

const channelsStore = useChannelsStore()
const userStore = useUserStore()

const showCreateModal = ref(false)

const groupChannels = computed(() => channelsStore.groupChannels)
const directChannels = computed(() => channelsStore.directChannels)

function isActiveChannel(channelId) {
  return channelsStore.currentChannelId === channelId
}

function selectChannel(channelId) {
  if (channelsStore.currentChannelId !== channelId) {
    // Clear unread count when selecting channel
    channelsStore.clearUnreadCount(channelId)

    // Emit event to parent to handle channel switch
    emit('channel-selected', channelId)
  }
}

function getDMInitials(name) {
  if (!name) return '?'
  return name.substring(0, 2).toUpperCase()
}

function handleChannelCreated(channel) {
  console.log('Channel created:', channel)
  // Channel is automatically added to store and switched to
}

const emit = defineEmits(['channel-selected'])
</script>

<style scoped>
.channel-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #2c2f33;
  color: #dcddde;
}

.channel-list-header {
  padding: 16px;
  border-bottom: 1px solid #23272a;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.channel-list-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
  color: white;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.add-channel-button {
  padding: 6px;
  background-color: transparent;
  border: none;
  color: #96989d;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-channel-button:hover {
  background-color: #393c43;
  color: white;
}

.channels-section {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  font-size: 12px;
  font-weight: 600;
  color: #96989d;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-top: 8px;
}

.section-icon {
  font-size: 14px;
  font-weight: 700;
}

.channel-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  margin: 2px 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  position: relative;
}

.channel-item:hover {
  background-color: #393c43;
  color: white;
}

.channel-item.active {
  background-color: #404449;
  color: white;
}

.channel-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: #5865f2;
  border-radius: 0 2px 2px 0;
}

.channel-hash {
  font-size: 18px;
  font-weight: 700;
  color: #96989d;
  opacity: 0.7;
}

.channel-item.active .channel-hash,
.channel-item:hover .channel-hash {
  color: white;
  opacity: 1;
}

.channel-name {
  flex: 1;
  font-size: 15px;
  font-weight: 500;
}

.unread-badge {
  padding: 2px 6px;
  background-color: #f23f42;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 700;
  color: white;
}

.dm-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-text {
  font-size: 10px;
  font-weight: 700;
  color: white;
}

.info-section {
  padding: 16px;
  border-top: 1px solid #23272a;
  background-color: #23272a;
}

.info-text {
  margin: 0;
  font-size: 11px;
  color: #72767d;
  line-height: 1.4;
}

/* Scrollbar styling */
.channels-section::-webkit-scrollbar {
  width: 6px;
}

.channels-section::-webkit-scrollbar-track {
  background: transparent;
}

.channels-section::-webkit-scrollbar-thumb {
  background: #1e2124;
  border-radius: 3px;
}

.channels-section::-webkit-scrollbar-thumb:hover {
  background: #2c2f33;
}

/* Responsive */
@media (max-width: 768px) {
  .channel-list-header h3 {
    font-size: 14px;
  }

  .phase-badge {
    font-size: 9px;
    padding: 3px 6px;
  }

  .channel-item {
    padding: 6px 12px;
  }
}
</style>
