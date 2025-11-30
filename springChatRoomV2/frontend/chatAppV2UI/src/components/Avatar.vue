<template>
  <div
    :class="['avatar', sizeClass, { clickable: clickable }]"
    :style="avatarStyle"
    @click="handleClick"
    :title="username"
  >
    <img
      v-if="avatarUrl"
      :src="avatarUrl"
      :alt="username"
      class="avatar-image"
      @error="handleImageError"
    />
    <span v-else class="avatar-initials">{{ initials }}</span>
    <span v-if="showOnline" :class="['online-indicator', { online: isOnline }]"></span>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  username: {
    type: String,
    required: true
  },
  avatarUrl: {
    type: String,
    default: null
  },
  size: {
    type: String,
    default: 'medium', // 'small', 'medium', 'large', 'xlarge'
    validator: (value) => ['small', 'medium', 'large', 'xlarge'].includes(value)
  },
  showOnline: {
    type: Boolean,
    default: false
  },
  isOnline: {
    type: Boolean,
    default: false
  },
  clickable: {
    type: Boolean,
    default: false
  },
  backgroundColor: {
    type: String,
    default: null
  }
})

const emit = defineEmits(['click'])

const imageError = ref(false)

const initials = computed(() => {
  if (!props.username) return '?'

  const words = props.username.trim().split(/\s+/)
  if (words.length >= 2) {
    return (words[0][0] + words[1][0]).toUpperCase()
  }
  return props.username.substring(0, 2).toUpperCase()
})

const sizeClass = computed(() => `avatar-${props.size}`)

const avatarStyle = computed(() => {
  if (props.backgroundColor) {
    return { backgroundColor: props.backgroundColor }
  }

  // Generate color based on username
  const hash = props.username.split('').reduce((acc, char) => {
    return char.charCodeAt(0) + ((acc << 5) - acc)
  }, 0)

  const hue = Math.abs(hash) % 360
  return {
    background: `linear-gradient(135deg, hsl(${hue}, 70%, 60%) 0%, hsl(${hue + 30}, 70%, 50%) 100%)`
  }
})

function handleClick() {
  if (props.clickable) {
    emit('click')
  }
}

function handleImageError() {
  imageError.value = true
}
</script>

<style scoped>
.avatar {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  font-weight: 700;
  color: white;
  user-select: none;
}

.avatar-small {
  width: 24px;
  height: 24px;
  font-size: 10px;
}

.avatar-medium {
  width: 32px;
  height: 32px;
  font-size: 12px;
}

.avatar-large {
  width: 48px;
  height: 48px;
  font-size: 18px;
}

.avatar-xlarge {
  width: 80px;
  height: 80px;
  font-size: 28px;
}

.avatar.clickable {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.avatar.clickable:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-initials {
  text-transform: uppercase;
}

.online-indicator {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 25%;
  height: 25%;
  min-width: 8px;
  min-height: 8px;
  border-radius: 50%;
  background-color: #757575;
  border: 2px solid white;
}

.online-indicator.online {
  background-color: #43b581;
}

/* Size-specific online indicators */
.avatar-small .online-indicator {
  width: 8px;
  height: 8px;
  border-width: 1.5px;
}

.avatar-medium .online-indicator {
  width: 10px;
  height: 10px;
  border-width: 2px;
}

.avatar-large .online-indicator {
  width: 14px;
  height: 14px;
  border-width: 2px;
}

.avatar-xlarge .online-indicator {
  width: 20px;
  height: 20px;
  border-width: 3px;
}
</style>
