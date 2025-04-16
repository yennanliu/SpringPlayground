<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-container">
      <div class="modal-header">
        <h2>Create a channel</h2>
        <button class="close-button" @click="$emit('close')">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <p class="modal-description">
          Channels are where your team communicates. They're best when organized around a topic — #marketing, for example.
        </p>
        
        <form @submit.prevent="createChannel">
          <div class="form-group">
            <label for="channelName">Name</label>
            <div class="input-prefix-container">
              <span class="input-prefix">#</span>
              <input 
                type="text" 
                id="channelName" 
                v-model="channelName" 
                placeholder="e.g. marketing"
                required
                maxlength="80"
                :class="{ 'has-error': !!nameError }"
              >
            </div>
            <div class="input-hint" :class="{ 'text-error': !!nameError }">
              {{ nameError || 'Channel names must be lowercase, without spaces or periods, and shorter than 80 characters.' }}
            </div>
          </div>
          
          <div class="form-group">
            <label for="channelDescription">Description <span class="optional">(optional)</span></label>
            <input 
              type="text" 
              id="channelDescription" 
              v-model="channelDescription" 
              placeholder="What's this channel about?"
              maxlength="250"
            >
            <div class="input-hint">
              What's this channel about?
            </div>
          </div>
          
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" v-model="isPrivate">
              <span>Make private</span>
            </label>
            <div class="input-hint">
              When a channel is private, it can only be viewed or joined by invitation.
            </div>
          </div>
          
          <div class="modal-footer">
            <button type="button" class="cancel-button" @click="$emit('close')">Cancel</button>
            <button 
              type="submit" 
              class="create-button" 
              :disabled="isLoading || !!nameError || !channelName"
            >
              <span v-if="isLoading">
                <i class="fas fa-circle-notch fa-spin"></i> Creating...
              </span>
              <span v-else>Create</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useChannelStore } from '@/stores/channelStore';

const props = defineProps({
  onSuccess: {
    type: Function,
    default: () => {}
  }
});

const emit = defineEmits(['close']);
const channelStore = useChannelStore();

const channelName = ref('');
const channelDescription = ref('');
const isPrivate = ref(false);
const isLoading = ref(false);
const error = ref('');

const nameError = computed(() => {
  if (!channelName.value) return '';
  
  // Check for lowercase
  if (channelName.value !== channelName.value.toLowerCase()) {
    return 'Channel names must be in lowercase.';
  }
  
  // Check for spaces
  if (channelName.value.includes(' ')) {
    return 'Channel names cannot contain spaces.';
  }
  
  // Check for periods
  if (channelName.value.includes('.')) {
    return 'Channel names cannot contain periods.';
  }
  
  // Check special characters
  if (!/^[a-z0-9_-]+$/.test(channelName.value)) {
    return 'Channel names can only contain lowercase letters, numbers, hyphens, and underscores.';
  }
  
  return '';
});

const createChannel = async () => {
  if (nameError.value || !channelName.value) return;
  
  isLoading.value = true;
  error.value = '';
  
  try {
    const newChannel = await channelStore.createChannel({
      name: channelName.value,
      description: channelDescription.value,
      isPrivate: isPrivate.value
    });
    
    props.onSuccess(newChannel);
    emit('close');
  } catch (err) {
    error.value = err.message || 'Failed to create channel. Please try again.';
  } finally {
    isLoading.value = false;
  }
};

// Auto-format the channel name to be lowercase
watch(channelName, (newValue) => {
  if (newValue) {
    channelName.value = newValue.toLowerCase();
  }
});
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background-color: white;
  border-radius: 8px;
  width: 95%;
  max-width: 520px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px 28px;
  border-bottom: 1px solid #e2e2e2;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  font-size: 22px;
  color: #1d1c1d;
}

.close-button {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #616061;
}

.modal-body {
  padding: 20px 28px;
}

.modal-description {
  margin-top: 0;
  margin-bottom: 24px;
  color: #616061;
  font-size: 15px;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #1d1c1d;
}

.optional {
  font-weight: normal;
  color: #616061;
}

.input-prefix-container {
  position: relative;
}

.input-prefix {
  position: absolute;
  left: 12px;
  top: 10px;
  color: #616061;
}

.input-prefix-container input {
  padding-left: 30px;
}

.form-group input[type="text"] {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 15px;
}

.form-group input.has-error {
  border-color: var(--slack-red);
}

.input-hint {
  margin-top: 4px;
  color: #616061;
  font-size: 13px;
}

.text-error {
  color: var(--slack-red);
}

.checkbox-label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox-label input {
  margin-right: 8px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 32px;
  padding-top: 16px;
  border-top: 1px solid #e2e2e2;
}

.cancel-button {
  background: none;
  border: none;
  padding: 10px 16px;
  cursor: pointer;
  color: #1d1c1d;
  font-size: 15px;
  margin-right: 12px;
}

.create-button {
  background-color: var(--slack-green);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 10px 16px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.create-button:hover:not(:disabled) {
  background-color: #239c67;
}

.create-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style> 