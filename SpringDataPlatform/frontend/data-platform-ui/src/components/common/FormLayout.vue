<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">{{ title }}</h4>
        <p v-if="subtitle" class="text-muted">{{ subtitle }}</p>
      </div>
    </div>

    <div class="row">
      <div :class="columnClasses">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FormLayout",
  props: {
    title: {
      type: String,
      required: true,
    },
    subtitle: {
      type: String,
      default: "",
    },
    width: {
      type: String,
      default: "medium",
      validator: (value) => ["narrow", "medium", "wide", "full"].includes(value),
    },
  },
  computed: {
    columnClasses() {
      const widthClasses = {
        narrow: "col-md-4 offset-md-4",
        medium: "col-md-6 offset-md-3",
        wide: "col-md-8 offset-md-2",
        full: "col-12",
      };
      return `${widthClasses[this.width]} px-5 px-md-0`;
    },
  },
};
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
