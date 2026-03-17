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

<script>
export default {
  name: "AlertMessage",
  props: {
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
      default: 0, // milliseconds, 0 = no auto dismiss
    },
  },
  data() {
    return {
      visible: true,
      timer: null,
    };
  },
  mounted() {
    if (this.autoDismiss > 0) {
      this.timer = setTimeout(() => {
        this.dismiss();
      }, this.autoDismiss);
    }
  },
  beforeDestroy() {
    if (this.timer) {
      clearTimeout(this.timer);
    }
  },
  methods: {
    dismiss() {
      this.visible = false;
      this.$emit("dismissed");
    },
  },
};
</script>

<style scoped>
.alert {
  border-radius: 4px;
  margin-bottom: 16px;
}
</style>
