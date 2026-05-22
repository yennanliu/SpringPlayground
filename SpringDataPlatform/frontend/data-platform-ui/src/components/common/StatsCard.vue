<template>
  <div class="stats-card" :class="`accent-${accent}`">
    <div class="stats-icon">
      <slot name="icon" />
    </div>
    <div class="stats-body">
      <div class="stats-value">
        <span v-if="loading" class="stats-skeleton" />
        <span v-else>{{ value }}</span>
      </div>
      <p class="stats-label">{{ label }}</p>
    </div>
    <div class="stats-trend" v-if="trend">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
        <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
        <polyline points="17 6 23 6 23 12"/>
      </svg>
      {{ trend }}
    </div>
  </div>
</template>

<script setup>
defineProps({
  value:   { type: [String, Number], default: '—' },
  label:   { type: String, required: true },
  accent:  { type: String, default: 'default' }, // default | blue | green | orange
  loading: { type: Boolean, default: false },
  trend:   { type: String, default: null },
})
</script>

<style scoped>
.stats-card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  padding: 22px 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid var(--color-gray-200);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition);
}
.stats-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.stats-icon {
  width: 48px; height: 48px;
  border-radius: var(--radius-md);
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.accent-default .stats-icon { background: var(--color-gray-100); color: var(--color-gray-600); }
.accent-blue    .stats-icon { background: var(--color-info-bg);    color: var(--color-info); }
.accent-green   .stats-icon { background: var(--color-success-bg); color: var(--color-success); }
.accent-orange  .stats-icon { background: var(--color-accent-light); color: var(--color-accent-dark); }
.accent-red     .stats-icon { background: var(--color-danger-bg); color: var(--color-danger); }

.stats-body { flex: 1; min-width: 0; }

.stats-value {
  font-size: 1.75rem;
  font-weight: 800;
  color: var(--color-gray-900);
  letter-spacing: -0.03em;
  line-height: 1;
  margin-bottom: 4px;
}

.stats-skeleton {
  display: inline-block;
  width: 48px; height: 28px;
  background: linear-gradient(90deg, var(--color-gray-200) 25%, var(--color-gray-100) 50%, var(--color-gray-200) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
  border-radius: var(--radius-sm);
  vertical-align: middle;
}
@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

.stats-label {
  font-size: 0.8rem;
  color: var(--color-gray-500);
  font-weight: 500;
  margin: 0;
}

.stats-trend {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 0.72rem;
  font-weight: 600;
  color: var(--color-success);
  background: var(--color-success-bg);
  padding: 3px 8px;
  border-radius: var(--radius-full);
  flex-shrink: 0;
}
</style>
