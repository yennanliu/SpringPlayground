<template>
  <div class="chat-area">
    <div class="messages-container" ref="messagesContainer">
      <div v-if="isLoading" class="loading">
        <i class="fas fa-circle-notch fa-spin"></i>
        Loading messages...
      </div>
      
      <div v-else-if="messages.length === 0" class="no-messages">
        <p>No messages yet. Start the conversation!</p>
      </div>
      
      <div v-else class="messages">
        <div 
          v-for="(message, index) in messages" 
          :key="message.id"
          class="message"
          :class="{ 'same-author': isSameAuthor(index) }"
        >
          <div class="avatar" v-if="!isSameAuthor(index)">
            <img 
              :src="message.author.avatar || '/default-avatar.png'" 
              :alt="message.author.displayName"
            >
          </div>
          <div class="message-content">
            <div class="message-header" v-if="!isSameAuthor(index)">
              <span class="author-name">{{ message.author.displayName }}</span>
              <span class="timestamp">{{ formatDate(message.createdAt) }}</span>
            </div>
            <div class="message-body" v-html="formatMessage(message.content)"></div>
            <div class="message-actions">
              <button @click="addReaction(message.id)">
                <i class="far fa-smile"></i>
              </button>
              <button @click="replyToMessage(message.id)">
                <i class="fas fa-reply"></i>
              </button>
              <button 
                v-if="message.author.id === currentUser?.id"
                @click="editMessage(message)"
              >
                <i class="fas fa-edit"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <MessageInput 
      :channelId="channelId" 
      @message-sent="onMessageSent"
      :replyTo="replyToMessage"
      :editMessage="editingMessage"
      @cancel-edit="cancelEdit"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useMessageStore } from '@/stores/messageStore';
import { format } from 'date-fns';
import MessageInput from './MessageInput.vue';

const props = defineProps({
  channelId: {
    type: String,
    required: true
  }
});

const userStore = useUserStore();
const messageStore = useMessageStore();
const messagesContainer = ref(null);
const replyToMessage = ref(null);
const editingMessage = ref(null);

const currentUser = computed(() => userStore.currentUser);
const messages = computed(() => messageStore.messages);
const isLoading = computed(() => messageStore.isLoading);

const formatDate = (date) => {
  return format(new Date(date), 'h:mm a');
};

const formatMessage = (content) => {
  // Replace URLs with links
  const withLinks = content.replace(
    /https?:\/\/\S+/g, 
    url => `<a href="${url}" target="_blank" rel="noopener">${url}</a>`
  );
  
  // Replace @mentions
  return withLinks.replace(
    /@(\w+)/g, 
    (match, username) => `<span class="mention">@${username}</span>`
  );
};

const isSameAuthor = (index) => {
  if (index === 0) return false;
  return messages.value[index].author.id === messages.value[index - 1].author.id;
};

const onMessageSent = () => {
  scrollToBottom();
  replyToMessage.value = null;
  editingMessage.value = null;
};

const addReaction = (messageId) => {
  // Opens emoji picker to add reaction
  console.log('Add reaction to message', messageId);
};

const replyToMessage = (messageId) => {
  const message = messages.value.find(m => m.id === messageId);
  if (message) {
    replyToMessage.value = message;
  }
};

const editMessage = (message) => {
  if (message.author.id === currentUser.value?.id) {
    editingMessage.value = message;
  }
};

const cancelEdit = () => {
  editingMessage.value = null;
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

watch(() => props.channelId, async (newId) => {
  if (newId) {
    await messageStore.fetchMessages(newId);
    scrollToBottom();
  }
});

onMounted(async () => {
  if (props.channelId) {
    await messageStore.fetchMessages(props.channelId);
    scrollToBottom();
  }
});
</script>

<style scoped>
.chat-area {
  display: flex;
  flex-direction: column;
  height: 100%;
  flex: 1;
  overflow: hidden;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.loading, .no-messages {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
  color: #888;
}

.loading i {
  margin-right: 8px;
}

.message {
  display: flex;
  margin-bottom: 8px;
  padding: 4px 0;
}

.message.same-author {
  margin-top: -4px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 8px;
  flex-shrink: 0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  flex: 1;
}

.message-header {
  margin-bottom: 4px;
}

.author-name {
  font-weight: bold;
  margin-right: 8px;
}

.timestamp {
  color: #888;
  font-size: 12px;
}

.message-body {
  line-height: 1.5;
}

.message-body a {
  color: var(--slack-blue);
  text-decoration: none;
}

.message-body .mention {
  color: var(--slack-blue);
  background: rgba(29, 155, 209, 0.1);
  border-radius: 3px;
  padding: 0 2px;
}

.message-actions {
  display: flex;
  gap: 8px;
  margin-top: 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.message:hover .message-actions {
  opacity: 1;
}

.message-actions button {
  background: none;
  border: none;
  cursor: pointer;
  color: #888;
  padding: 2px 4px;
}

.message-actions button:hover {
  color: var(--slack-blue);
}
</style> 