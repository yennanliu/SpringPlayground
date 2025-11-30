<template>
  <div v-if="typingUsers.length > 0" class="typing-indicator">
    <div class="typing-dots">
      <span class="dot"></span>
      <span class="dot"></span>
      <span class="dot"></span>
    </div>
    <span class="typing-text">{{ typingText }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  typingUsers: {
    type: Array,
    default: () => []
  }
})

const typingText = computed(() => {
  const count = props.typingUsers.length

  if (count === 0) return ''
  if (count === 1) return `${props.typingUsers[0]} is typing...`
  if (count === 2) return `${props.typingUsers[0]} and ${props.typingUsers[1]} are typing...`
  return `${props.typingUsers[0]} and ${count - 1} others are typing...`
})
</script>

<style scoped>
.typing-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  background-color: #f9f9f9;
  border-top: 1px solid #e0e0e0;
  min-height: 40px;
}

.typing-dots {
  display: flex;
  gap: 4px;
  align-items: center;
}

.dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #667eea;
  animation: bounce 1.4s infinite ease-in-out;
}

.dot:nth-child(1) {
  animation-delay: -0.32s;
}

.dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0.7);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.typing-text {
  font-size: 13px;
  color: #666;
  font-style: italic;
}

/* Responsive */
@media (max-width: 768px) {
  .typing-indicator {
    padding: 6px 16px;
    min-height: 36px;
  }

  .typing-text {
    font-size: 12px;
  }

  .dot {
    width: 5px;
    height: 5px;
  }
}
</style>
