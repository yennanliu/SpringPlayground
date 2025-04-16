import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { 
  apiGetChannels, 
  apiGetChannel, 
  apiCreateChannel, 
  apiJoinChannel 
} from '@/services/api';

export const useChannelStore = defineStore('channel', () => {
  const channels = ref([]);
  const currentChannel = ref(null);
  const isLoading = ref(false);
  const error = ref(null);

  const publicChannels = computed(() => 
    channels.value.filter(channel => !channel.isPrivate)
  );

  const privateChannels = computed(() => 
    channels.value.filter(channel => channel.isPrivate)
  );

  async function fetchChannels() {
    isLoading.value = true;
    try {
      channels.value = await apiGetChannels();
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  async function fetchChannel(channelId) {
    isLoading.value = true;
    try {
      currentChannel.value = await apiGetChannel(channelId);
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  async function createChannel(channelData) {
    isLoading.value = true;
    try {
      const newChannel = await apiCreateChannel(channelData);
      channels.value.push(newChannel);
      return newChannel;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      isLoading.value = false;
    }
  }

  async function joinChannel(channelId) {
    isLoading.value = true;
    try {
      await apiJoinChannel(channelId);
      await fetchChannels();
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  return {
    channels,
    currentChannel,
    isLoading,
    error,
    publicChannels,
    privateChannels,
    fetchChannels,
    fetchChannel,
    createChannel,
    joinChannel
  };
}); 