<template>
  <div class="channel-list">
    <div class="channel-list-header">
      <h3>Channels</h3>
      <span class="phase-badge">Phase 1 MVP</span>
    </div>

    <div class="channels-section">
      <div class="section-title">
        <span class="section-icon">#</span>
        <span>Group Channels</span>
      </div>

      <div
        v-for="channel in channels"
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
    </div>

    <div class="info-section">
      <p class="info-text">
        Phase 2 will add multi-channel support and direct messaging.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useMessagesStore } from '../stores/messages'

const messageStore = useMessagesStore()

// Phase 1: Single hardcoded "General" channel
const channels = ref([
  {
    id: 'group:general',
    name: 'general',
    type: 'group',
    unreadCount: 0
  }
])

function isActiveChannel(channelId) {
  return messageStore.currentChannel === channelId
}

function selectChannel(channelId) {
  if (messageStore.currentChannel !== channelId) {
    messageStore.setCurrentChannel(channelId)
    messageStore.clearMessages()

    // In Phase 2, we would:
    // 1. Unsubscribe from old channel
    // 2. Subscribe to new channel
    // 3. Load message history
    console.log('Selected channel:', channelId)
  }
}
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

.phase-badge {
  padding: 4px 8px;
  background-color: #5865f2;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 600;
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
