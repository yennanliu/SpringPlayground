<template>
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
      :class="{ 'is-invalid': hasError }"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :placeholder="placeholder"
      :disabled="disabled"
    />

    <!-- Textarea -->
    <textarea
      v-else-if="type === 'textarea'"
      class="form-control"
      :class="{ 'is-invalid': hasError }"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :placeholder="placeholder"
      :disabled="disabled"
      :rows="rows"
    ></textarea>

    <!-- Select -->
    <select
      v-else-if="type === 'select'"
      class="form-control"
      :class="{ 'is-invalid': hasError }"
      :value="modelValue"
      @change="$emit('update:modelValue', $event.target.value)"
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
      :class="{ 'is-invalid': hasError }"
      @change="handleFileChange"
      :accept="accept"
      :disabled="disabled"
    />

    <span v-if="error" class="invalid-feedback d-block">{{ error }}</span>
    <small v-if="helpText" class="form-text text-muted">{{ helpText }}</small>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  label: {
    type: String,
    default: "",
  },
  type: {
    type: String,
    default: "text",
  },
  modelValue: {
    type: [String, Number, File],
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  error: {
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
  rows: {
    type: Number,
    default: 3,
  },
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
  accept: {
    type: String,
    default: "",
  },
})

const emit = defineEmits(['update:modelValue', 'file-change'])

const fileInput = ref(null)

const isTextInput = computed(() => {
  return ["text", "email", "password", "number", "tel", "url"].includes(props.type)
})

const hasError = computed(() => !!props.error)

const getOptionValue = (option) => {
  return typeof option === "object" ? option[props.optionValue] : option
}

const getOptionLabel = (option) => {
  return typeof option === "object" ? option[props.optionLabel] : option
}

const handleFileChange = (event) => {
  const file = event.target.files[0]
  emit('update:modelValue', file)
  emit('file-change', file)
}
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
