<template>
  <div id="app">
    <Navbar v-if="showNav" />
    <main :class="['main-content', { 'with-nav': showNav }]">
      <router-view v-slot="{ Component, route }">
        <transition :name="route.meta.transition || 'fade'" mode="out-in">
          <component :is="Component" :key="route.path" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Navbar from './components/Navbar.vue'

const route = useRoute()
const showNav = computed(() => !['Signup', 'Signin'].includes(route.name))
</script>

<style>
/* ── Reset & base ─────────────────────────────────────────────────────── */
body { margin: 0; padding: 0; background: var(--color-gray-50); }

#app {
  font-family: var(--font-sans);
  color: var(--color-gray-900);
  -webkit-font-smoothing: antialiased;
}

.main-content {
  min-height: 100vh;
}
.main-content.with-nav {
  min-height: calc(100vh - 72px);
}

/* ── Global typography ────────────────────────────────────────────────── */
h1, h2, h3, h4, h5, h6 {
  font-family: var(--font-sans);
  font-weight: 700;
  letter-spacing: -0.02em;
  margin: 0 0 12px;
  color: var(--color-gray-900);
}
a { text-decoration: none; color: inherit; }

/* ── Global button system ─────────────────────────────────────────────── */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: var(--radius-md);
  font-family: var(--font-sans);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all var(--transition);
  white-space: nowrap;
}
.btn:disabled { opacity: 0.55; cursor: not-allowed; transform: none !important; }

.btn-primary {
  background: var(--color-black);
  color: var(--color-white);
}
.btn-primary:hover:not(:disabled) {
  background: var(--color-gray-800);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
  color: var(--color-white);
}

.btn-accent {
  background: var(--color-accent);
  color: var(--color-black);
}
.btn-accent:hover:not(:disabled) {
  background: var(--color-accent-dark);
  transform: translateY(-1px);
  box-shadow: var(--shadow-accent);
}

.btn-ghost {
  background: transparent;
  color: var(--color-gray-700);
  border: 1.5px solid var(--color-gray-200);
}
.btn-ghost:hover:not(:disabled) {
  background: var(--color-gray-100);
  border-color: var(--color-gray-300);
  color: var(--color-gray-900);
}

.btn-danger {
  background: var(--color-danger-bg);
  color: var(--color-danger);
  border: 1.5px solid transparent;
}
.btn-danger:hover:not(:disabled) {
  background: var(--color-danger);
  color: var(--color-white);
}

.btn-sm { padding: 6px 14px; font-size: 0.8rem; }
.btn-lg { padding: 14px 28px; font-size: 1rem; }

/* ── Container ────────────────────────────────────────────────────────── */
.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* ── Page layout helpers ──────────────────────────────────────────────── */
.page-wrap { padding: 40px 0 60px; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 28px;
}
.page-title  { font-size: 1.6rem; font-weight: 700; margin: 0; }
.page-subtitle { color: var(--color-gray-500); margin: 4px 0 0; font-size: 0.9rem; }

/* ── Card ─────────────────────────────────────────────────────────────── */
.card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--color-gray-200);
  overflow: hidden;
}

/* ── Table ────────────────────────────────────────────────────────────── */
.table-card {
  background: var(--color-white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--color-gray-200);
  overflow: hidden;
}
.tbl { width: 100%; border-collapse: collapse; }
.tbl th {
  padding: 13px 20px;
  text-align: left;
  font-size: 0.72rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--color-gray-500);
  background: var(--color-gray-50);
  border-bottom: 1px solid var(--color-gray-200);
}
.tbl td {
  padding: 14px 20px;
  border-bottom: 1px solid var(--color-gray-100);
  font-size: 0.9rem;
  vertical-align: middle;
}
.tbl tbody tr:last-child td { border-bottom: none; }
.tbl tbody tr { transition: background var(--transition); }
.tbl tbody tr:hover { background: var(--color-gray-50); }

/* ── Badge ────────────────────────────────────────────────────────────── */
.badge {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 10px;
  border-radius: var(--radius-full);
  font-size: 0.72rem;
  font-weight: 600;
  letter-spacing: 0.02em;
}
.badge-dot { width: 5px; height: 5px; border-radius: 50%; background: currentColor; }
.badge-success { background: var(--color-success-bg); color: var(--color-success); }
.badge-danger  { background: var(--color-danger-bg);  color: var(--color-danger); }
.badge-warning { background: var(--color-warning-bg); color: var(--color-warning); }
.badge-info    { background: var(--color-info-bg);    color: var(--color-info); }
.badge-neutral { background: var(--color-gray-100);   color: var(--color-gray-500); }
.badge-success .badge-dot { animation: pulse-dot 2s ease-in-out infinite; }
@keyframes pulse-dot {
  0%,100% { transform: scale(1); opacity: 1; }
  50%      { transform: scale(1.5); opacity: 0.6; }
}

/* ── States ───────────────────────────────────────────────────────────── */
.state-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 64px 24px;
  text-align: center;
}
.state-icon {
  width: 72px; height: 72px;
  border-radius: 50%;
  background: var(--color-gray-100);
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 20px;
  color: var(--color-gray-400);
}
.state-title { font-size: 1.05rem; font-weight: 600; color: var(--color-gray-800); margin: 0 0 6px; }
.state-desc  { font-size: 0.88rem; color: var(--color-gray-500); margin: 0 0 20px; max-width: 300px; }

.alert-danger {
  display: flex; align-items: center; gap: 10px;
  padding: 14px 18px;
  background: var(--color-danger-bg);
  border: 1px solid rgba(239,68,68,.2);
  border-radius: var(--radius-md);
  color: var(--color-danger);
  font-size: 0.9rem; font-weight: 500;
}

/* ── Spinner ──────────────────────────────────────────────────────────── */
.spinner {
  width: 32px; height: 32px;
  border: 3px solid var(--color-gray-200);
  border-top-color: var(--color-black);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
.spinner-sm { width: 16px; height: 16px; border-width: 2px; }
@keyframes spin { to { transform: rotate(360deg); } }

/* ── Page transitions ─────────────────────────────────────────────────── */
.fade-enter-active, .fade-leave-active { transition: opacity 0.18s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.slide-enter-active, .slide-leave-active {
  transition: opacity 0.22s ease, transform 0.22s ease;
}
.slide-enter-from { opacity: 0; transform: translateY(10px); }
.slide-leave-to   { opacity: 0; transform: translateY(-6px); }

/* ── Mobile ───────────────────────────────────────────────────────────── */
@media (max-width: 768px) {
  .page-header { flex-direction: column; gap: 14px; }
  .page-title  { font-size: 1.35rem; }
  .container   { padding: 0 16px; }
}
</style>
