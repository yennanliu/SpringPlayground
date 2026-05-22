<template>
  <nav class="navbar">
    <div class="navbar-inner">

      <!-- Logo -->
      <router-link :to="{ name: 'Home' }" class="navbar-brand">
        <div class="brand-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
          </svg>
        </div>
        <span class="brand-name">DataPlatform</span>
      </router-link>

      <!-- Desktop nav -->
      <div class="nav-links" :class="{ open: mobileOpen }">

        <div class="nav-group" v-for="group in navGroups" :key="group.label">
          <button class="nav-group-btn" @click="toggleGroup(group.label)">
            {{ group.label }}
            <svg class="caret" :class="{ rotated: openGroup === group.label }"
              width="14" height="14" viewBox="0 0 24 24" fill="none"
              stroke="currentColor" stroke-width="2.5">
              <path d="M6 9l6 6 6-6"/>
            </svg>
          </button>

          <div class="dropdown" :class="{ visible: openGroup === group.label }">
            <router-link
              v-for="item in group.items"
              :key="item.name"
              :to="{ name: item.name }"
              class="dropdown-item"
              @click="closeAll"
            >
              <span class="item-icon" v-html="item.icon"></span>
              <span>{{ item.label }}</span>
            </router-link>
          </div>
        </div>

      </div>

      <!-- Auth -->
      <div class="nav-auth">
        <template v-if="!token">
          <router-link :to="{ name: 'Signin' }" class="btn-nav-ghost">Sign in</router-link>
          <router-link :to="{ name: 'Signup' }" class="btn-nav-accent">Get started</router-link>
        </template>
        <template v-else>
          <router-link :to="{ name: 'Admin' }" class="btn-nav-ghost">Admin</router-link>
          <button @click="handleSignout" class="btn-nav-ghost signout">Sign out</button>
        </template>
      </div>

      <!-- Hamburger -->
      <button class="hamburger" :class="{ active: mobileOpen }" @click="mobileOpen = !mobileOpen" aria-label="Menu">
        <span></span><span></span><span></span>
      </button>
    </div>

    <!-- Mobile drawer -->
    <div class="mobile-drawer" :class="{ open: mobileOpen }">
      <div v-for="group in navGroups" :key="group.label" class="mobile-group">
        <p class="mobile-group-label">{{ group.label }}</p>
        <router-link
          v-for="item in group.items"
          :key="item.name"
          :to="{ name: item.name }"
          class="mobile-link"
          @click="mobileOpen = false"
        >{{ item.label }}</router-link>
      </div>
      <div class="mobile-auth">
        <template v-if="!token">
          <router-link :to="{ name: 'Signin' }" class="mobile-link" @click="mobileOpen = false">Sign in</router-link>
          <router-link :to="{ name: 'Signup' }" class="mobile-link accent" @click="mobileOpen = false">Get started</router-link>
        </template>
        <button v-else @click="handleSignout" class="mobile-link">Sign out</button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import swal from 'sweetalert'
import { useAuthStore } from '@/stores'

const router   = useRouter()
const authStore = useAuthStore()
const token     = computed(() => authStore.token)

const mobileOpen = ref(false)
const openGroup  = ref(null)

const toggleGroup = (label) => {
  openGroup.value = openGroup.value === label ? null : label
}
const closeAll = () => {
  openGroup.value = null
  mobileOpen.value = false
}

const handleSignout = () => {
  authStore.signout()
  closeAll()
  router.push({ name: 'Home' })
  swal({ text: 'You have been signed out.', icon: 'success', closeOnClickOutside: false })
}

const navGroups = [
  {
    label: 'Clusters',
    items: [
      { name: 'ListCluster', label: 'All Clusters',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="2" width="20" height="8" rx="2"/><rect x="2" y="14" width="20" height="8" rx="2"/></svg>' },
      { name: 'AddCluster', label: 'Add Cluster',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>' },
    ],
  },
  {
    label: 'Flink Jobs',
    items: [
      { name: 'ListJob', label: 'All Jobs',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="5 3 19 12 5 21 5 3"/></svg>' },
      { name: 'AddJob', label: 'Submit JAR Job',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>' },
      { name: 'AddSqlJob', label: 'Submit SQL Job',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/></svg>' },
    ],
  },
  {
    label: 'JAR Files',
    items: [
      { name: 'ListJar', label: 'All JARs',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/></svg>' },
      { name: 'AddJar', label: 'Upload JAR',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>' },
    ],
  },
  {
    label: 'Zeppelin',
    items: [
      { name: 'ListNotebook', label: 'Notebooks',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>' },
      { name: 'AddNotebook', label: 'New Notebook',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>' },
      { name: 'NotebookConsole', label: 'Console',
        icon: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="4 17 10 11 4 5"/><line x1="12" y1="19" x2="20" y2="19"/></svg>' },
    ],
  },
]
</script>

<style scoped>
/* ── Shell ──────────────────────────────────────────────────────────────── */
.navbar {
  background: var(--color-black);
  position: sticky;
  top: 0;
  z-index: 500;
  box-shadow: 0 1px 0 rgba(255,255,255,.08);
}
.navbar-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  gap: 24px;
}

/* ── Brand ──────────────────────────────────────────────────────────────── */
.navbar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
}
.brand-icon {
  width: 34px; height: 34px;
  background: var(--color-accent);
  border-radius: var(--radius-sm);
  display: flex; align-items: center; justify-content: center;
  color: var(--color-black);
  transition: transform var(--transition);
}
.navbar-brand:hover .brand-icon { transform: rotate(-8deg) scale(1.05); }
.brand-name {
  font-size: 0.95rem;
  font-weight: 700;
  color: var(--color-white);
  letter-spacing: -0.01em;
}

/* ── Desktop nav ────────────────────────────────────────────────────────── */
.nav-links {
  display: flex;
  align-items: center;
  gap: 2px;
  flex: 1;
}
.nav-group { position: relative; }
.nav-group-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 12px;
  background: none;
  border: none;
  color: rgba(255,255,255,.75);
  font-family: var(--font-sans);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  border-radius: var(--radius-md);
  transition: all var(--transition);
}
.nav-group-btn:hover,
.nav-group:focus-within .nav-group-btn {
  background: rgba(255,255,255,.08);
  color: var(--color-white);
}
.caret { transition: transform var(--transition); }
.caret.rotated { transform: rotate(180deg); }

.dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  min-width: 210px;
  background: var(--color-white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--color-gray-200);
  padding: 6px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-6px);
  transition: opacity var(--transition), transform var(--transition), visibility var(--transition);
  z-index: 100;
}
.dropdown.visible {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}
.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: var(--radius-sm);
  color: var(--color-gray-700);
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  transition: all var(--transition);
}
.dropdown-item:hover {
  background: var(--color-gray-50);
  color: var(--color-gray-900);
}
.dropdown-item.router-link-active {
  background: var(--color-accent-light);
  color: var(--color-black);
}
.item-icon { color: var(--color-gray-400); display: flex; align-items: center; flex-shrink: 0; }

/* ── Auth buttons ───────────────────────────────────────────────────────── */
.nav-auth {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}
.btn-nav-ghost {
  padding: 7px 16px;
  background: none;
  border: 1.5px solid rgba(255,255,255,.18);
  border-radius: var(--radius-md);
  color: rgba(255,255,255,.8);
  font-family: var(--font-sans);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: all var(--transition);
}
.btn-nav-ghost:hover { border-color: rgba(255,255,255,.5); color: var(--color-white); }
.btn-nav-ghost.signout { background: none; }
.btn-nav-accent {
  padding: 7px 16px;
  background: var(--color-accent);
  border: none;
  border-radius: var(--radius-md);
  color: var(--color-black);
  font-family: var(--font-sans);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
  transition: all var(--transition);
}
.btn-nav-accent:hover { background: var(--color-accent-dark); transform: translateY(-1px); }

/* ── Hamburger ──────────────────────────────────────────────────────────── */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
  width: 36px; height: 36px;
  padding: 6px;
  background: none;
  border: none;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: background var(--transition);
}
.hamburger:hover { background: rgba(255,255,255,.08); }
.hamburger span {
  display: block;
  width: 100%; height: 2px;
  background: var(--color-white);
  border-radius: 2px;
  transition: all 0.3s ease;
}
.hamburger.active span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.active span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.active span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

/* ── Mobile drawer ──────────────────────────────────────────────────────── */
.mobile-drawer {
  background: var(--color-gray-900);
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.3s ease;
}
.mobile-drawer.open { max-height: 600px; }

.mobile-group { padding: 12px 20px 4px; }
.mobile-group-label {
  font-size: 0.7rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--color-gray-500);
  margin: 0 0 4px;
}
.mobile-link {
  display: block;
  padding: 10px 8px;
  color: rgba(255,255,255,.7);
  font-size: 0.9rem;
  font-weight: 500;
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all var(--transition);
  background: none;
  border: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
  font-family: var(--font-sans);
}
.mobile-link:hover { color: var(--color-white); background: rgba(255,255,255,.06); }
.mobile-link.accent { color: var(--color-accent); }
.mobile-auth { padding: 12px 20px 16px; border-top: 1px solid rgba(255,255,255,.08); margin-top: 8px; }

/* ── Responsive ─────────────────────────────────────────────────────────── */
@media (max-width: 900px) {
  .nav-links, .nav-auth { display: none; }
  .hamburger { display: flex; margin-left: auto; }
}
</style>
