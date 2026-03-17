<template>
  <div class="loading-spinner" :class="{ 'loading-spinner--centered': centered }">
    <div
      class="spinner-border"
      :class="[`text-${variant}`, sizeClass]"
      role="status"
    >
      <span class="sr-only">{{ loadingText }}</span>
    </div>
    <span v-if="showText" class="loading-text">{{ loadingText }}</span>
  </div>
</template>

<script>
export default {
  name: "LoadingSpinner",
  props: {
    size: {
      type: String,
      default: "md",
      validator: (value) => ["sm", "md", "lg"].includes(value),
    },
    variant: {
      type: String,
      default: "primary",
    },
    centered: {
      type: Boolean,
      default: false,
    },
    showText: {
      type: Boolean,
      default: false,
    },
    loadingText: {
      type: String,
      default: "Loading...",
    },
  },
  computed: {
    sizeClass() {
      const sizes = {
        sm: "spinner-border-sm",
        md: "",
        lg: "spinner-border-lg",
      };
      return sizes[this.size];
    },
  },
};
</script>

<style scoped>
.loading-spinner {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.loading-spinner--centered {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 20px;
}

.loading-text {
  color: #666;
  font-size: 14px;
}

.spinner-border-lg {
  width: 3rem;
  height: 3rem;
}
</style>
