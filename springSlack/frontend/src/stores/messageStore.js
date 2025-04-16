import { defineStore } from 'pinia';
import { ref } from 'vue';
import { 
  apiGetMessages, 
  apiSendMessage, 
  apiEditMessage, 
  apiDeleteMessage 
} from '@/services/api';

export const useMessageStore = defineStore('message', () => {
  const messages = ref([]);
  const isLoading = ref(false);
  const error = ref(null);

  async function fetchMessages(channelId) {
    isLoading.value = true;
    try {
      messages.value = await apiGetMessages(channelId);
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  async function sendMessage(channelId, content) {
    isLoading.value = true;
    try {
      const newMessage = await apiSendMessage(channelId, content);
      messages.value.push(newMessage);
      return newMessage;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      isLoading.value = false;
    }
  }

  async function editMessage(messageId, content) {
    isLoading.value = true;
    try {
      const updatedMessage = await apiEditMessage(messageId, content);
      const index = messages.value.findIndex(msg => msg.id === messageId);
      if (index !== -1) {
        messages.value[index] = updatedMessage;
      }
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  async function deleteMessage(messageId) {
    isLoading.value = true;
    try {
      await apiDeleteMessage(messageId);
      messages.value = messages.value.filter(msg => msg.id !== messageId);
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  }

  return {
    messages,
    isLoading,
    error,
    fetchMessages,
    sendMessage,
    editMessage,
    deleteMessage
  };
}); 