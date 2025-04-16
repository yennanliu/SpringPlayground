<template>
  <div class="message-input-container">
    <div v-if="replyTo" class="reply-bar">
      <span>Replying to {{ replyTo.author.displayName }}</span>
      <button @click="$emit('cancel-reply')" class="cancel-button">
        <i class="fas fa-times"></i>
      </button>
    </div>
    
    <div v-if="editMessage" class="edit-bar">
      <span>Editing message</span>
      <button @click="$emit('cancel-edit')" class="cancel-button">
        <i class="fas fa-times"></i>
      </button>
    </div>
    
    <div class="input-area">
      <div class="formatting-buttons">
        <button title="Bold" @click="applyFormat('bold')">
          <i class="fas fa-bold"></i>
        </button>
        <button title="Italic" @click="applyFormat('italic')">
          <i class="fas fa-italic"></i>
        </button>
        <button title="Code" @click="applyFormat('code')">
          <i class="fas fa-code"></i>
        </button>
        <button title="Link" @click="applyFormat('link')">
          <i class="fas fa-link"></i>
        </button>
        <button title="List" @click="applyFormat('list')">
          <i class="fas fa-list"></i>
        </button>
      </div>
      
      <textarea
        ref="messageInput"
        v-model="messageText"
        @keydown="onKeyDown"
        placeholder="Message #channel"
        class="message-textarea"
      ></textarea>
      
      <div class="input-actions">
        <button class="action-button" title="Add emoji" @click="openEmojiPicker">
          <i class="far fa-smile"></i>
        </button>
        <button class="action-button" title="Attach file" @click="attachFile">
          <i class="fas fa-paperclip"></i>
        </button>
        <button 
          class="send-button" 
          :disabled="!messageText.trim()" 
          @click="sendMessage"
        >
          <i class="fas fa-paper-plane"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useMessageStore } from '@/stores/messageStore';

const props = defineProps({
  channelId: {
    type: String,
    required: true
  },
  replyTo: {
    type: Object,
    default: null
  },
  editMessage: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['message-sent', 'cancel-reply', 'cancel-edit']);

const messageStore = useMessageStore();
const messageText = ref('');
const messageInput = ref(null);

// Set message text if editing
watch(() => props.editMessage, (newVal) => {
  if (newVal) {
    messageText.value = newVal.content;
    focusInput();
  }
});

const focusInput = () => {
  setTimeout(() => {
    if (messageInput.value) {
      messageInput.value.focus();
    }
  }, 0);
};

const onKeyDown = (e) => {
  // Send on Enter without shift
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
};

const sendMessage = async () => {
  if (!messageText.value.trim()) return;
  
  try {
    if (props.editMessage) {
      await messageStore.editMessage(props.editMessage.id, messageText.value);
    } else {
      await messageStore.sendMessage(props.channelId, messageText.value);
    }
    messageText.value = '';
    emit('message-sent');
  } catch (error) {
    console.error('Failed to send message:', error);
  }
};

const applyFormat = (format) => {
  const textarea = messageInput.value;
  const start = textarea.selectionStart;
  const end = textarea.selectionEnd;
  const selectedText = messageText.value.substring(start, end);
  
  let formattedText = '';
  
  switch (format) {
    case 'bold':
      formattedText = `*${selectedText}*`;
      break;
    case 'italic':
      formattedText = `_${selectedText}_`;
      break;
    case 'code':
      formattedText = `\`${selectedText}\``;
      break;
    case 'link':
      formattedText = `<${selectedText || 'https://'}|${selectedText || 'link'}>`;
      break;
    case 'list':
      formattedText = `\n• ${selectedText}`;
      break;
  }
  
  messageText.value = messageText.value.substring(0, start) + formattedText + messageText.value.substring(end);
  textarea.focus();
  
  // Set cursor position
  const newCursorPos = start + formattedText.length;
  textarea.setSelectionRange(newCursorPos, newCursorPos);
};

const openEmojiPicker = () => {
  // In a real app, this would open an emoji picker component
  const emoji = prompt('Enter emoji:');
  if (emoji) {
    messageText.value += emoji;
  }
};

const attachFile = () => {
  // Would normally trigger a file input
  alert('File attachment feature would be implemented here');
};

onMounted(() => {
  focusInput();
});
</script>

<style scoped>
.message-input-container {
  border-top: 1px solid #e2e2e2;
  padding: 10px 20px 20px;
  background-color: white;
}

.reply-bar, .edit-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  background-color: #f8f8f8;
  border-radius: 4px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.cancel-button {
  background: none;
  border: none;
  cursor: pointer;
  color: #888;
}

.input-area {
  border: 1px solid #ccc;
  border-radius: 4px;
  overflow: hidden;
}

.formatting-buttons {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 6px 10px;
}

.formatting-buttons button {
  background: none;
  border: none;
  font-size: 14px;
  padding: 4px 6px;
  margin-right: 4px;
  cursor: pointer;
  color: #666;
  border-radius: 3px;
}

.formatting-buttons button:hover {
  background-color: #f2f2f2;
}

.message-textarea {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: none;
  resize: none;
  font-family: inherit;
  font-size: 15px;
  outline: none;
}

.input-actions {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-top: 1px solid #eee;
}

.action-button {
  background: none;
  border: none;
  font-size: 18px;
  padding: 4px 8px;
  margin-right: 8px;
  cursor: pointer;
  color: #888;
}

.action-button:hover {
  color: var(--slack-blue);
}

.send-button {
  margin-left: auto;
  background-color: var(--slack-green);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.send-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.send-button:hover:not(:disabled) {
  background-color: #239c67;
}
</style> 