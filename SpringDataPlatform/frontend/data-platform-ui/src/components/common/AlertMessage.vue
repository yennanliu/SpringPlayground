<template>
  <div
    v-if="visible"
    class="alert"
    :class="[`alert-${variant}`, { 'alert-dismissible': dismissible }]"
    role="alert"
  >
    <slot>{{ message }}</slot>
    <button
      v-if="dismissible"
      type="button"
      class="close"
      @click="dismiss"
      aria-label="Close"
    >
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  message: {
    type: String,
    default: "",
  },
  variant: {
    type: String,
    default: "info",
    validator: (value) =>
      ["success", "danger", "warning", "info", "primary", "secondary"].includes(value),
  },
  dismissible: {
    type: Boolean,
    default: false,
  },
  autoDismiss: {
    type: Number,
    default: 0,
  },
})

const emit = defineEmits(['dismissed'])

const visible = ref(true)
let timer = null

const dismiss = () => {
  visible.value = false
  emit('dismissed')
}

onMounted(() => {
  if (props.autoDismiss > 0) {
    timer = setTimeout(() => {
      dismiss()
    }, props.autoDismiss)
  }
})

onBeforeUnmount(() => {
  if (timer) {
    clearTimeout(timer)
  }
})
</script>

<style scoped>
.alert {
  border-radius: 4px;
  margin-bottom: 16px;
}
</style>
