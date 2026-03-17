<template>
  <ValidationProvider
    :name="label"
    :rules="rules"
    :vid="vid"
    v-slot="{ errors }"
  >
    <div class="form-group">
      <label v-if="label">
        {{ label }}
        <span v-if="required" class="text-danger">*</span>
      </label>

      <!-- Text/Email/Password Input -->
      <input
        v-if="isTextInput"
        :type="type"
        class="form-control"
        :class="{ 'is-invalid': errors[0] }"
        :value="value"
        @input="$emit('input', $event.target.value)"
        :placeholder="placeholder"
        :disabled="disabled"
      />

      <!-- Textarea -->
      <textarea
        v-else-if="type === 'textarea'"
        class="form-control"
        :class="{ 'is-invalid': errors[0] }"
        :value="value"
        @input="$emit('input', $event.target.value)"
        :placeholder="placeholder"
        :disabled="disabled"
        :rows="rows"
      ></textarea>

      <!-- Select -->
      <select
        v-else-if="type === 'select'"
        class="form-control"
        :class="{ 'is-invalid': errors[0] }"
        :value="value"
        @change="$emit('input', $event.target.value)"
        :disabled="disabled"
      >
        <option v-if="placeholder" value="" disabled selected>
          {{ placeholder }}
        </option>
        <option
          v-for="option in options"
          :key="getOptionValue(option)"
          :value="getOptionValue(option)"
        >
          {{ getOptionLabel(option) }}
        </option>
      </select>

      <!-- File Input -->
      <input
        v-else-if="type === 'file'"
        type="file"
        ref="fileInput"
        class="form-control"
        :class="{ 'is-invalid': errors[0] }"
        @change="handleFileChange"
        :accept="accept"
        :disabled="disabled"
      />

      <span v-if="errors[0]" class="invalid-feedback">{{ errors[0] }}</span>
      <small v-if="helpText" class="form-text text-muted">{{ helpText }}</small>
    </div>
  </ValidationProvider>
</template>

<script>
export default {
  name: "FormField",
  props: {
    label: {
      type: String,
      default: "",
    },
    type: {
      type: String,
      default: "text",
    },
    value: {
      type: [String, Number, File],
      default: "",
    },
    placeholder: {
      type: String,
      default: "",
    },
    rules: {
      type: String,
      default: "",
    },
    vid: {
      type: String,
      default: "",
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    required: {
      type: Boolean,
      default: false,
    },
    helpText: {
      type: String,
      default: "",
    },
    // Textarea specific
    rows: {
      type: Number,
      default: 3,
    },
    // Select specific
    options: {
      type: Array,
      default: () => [],
    },
    optionValue: {
      type: String,
      default: "value",
    },
    optionLabel: {
      type: String,
      default: "label",
    },
    // File specific
    accept: {
      type: String,
      default: "",
    },
  },
  computed: {
    isTextInput() {
      return ["text", "email", "password", "number", "tel", "url"].includes(this.type);
    },
  },
  methods: {
    getOptionValue(option) {
      return typeof option === "object" ? option[this.optionValue] : option;
    },
    getOptionLabel(option) {
      return typeof option === "object" ? option[this.optionLabel] : option;
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      this.$emit("input", file);
      this.$emit("file-change", file);
    },
  },
};
</script>

<style scoped>
.form-group {
  margin-bottom: 1rem;
}

label {
  font-weight: 500;
  margin-bottom: 0.5rem;
}
</style>
