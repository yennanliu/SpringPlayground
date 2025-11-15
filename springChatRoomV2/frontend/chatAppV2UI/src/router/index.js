import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import LoginView from '../views/LoginView.vue'
import ChatView from '../views/ChatView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresAuth: false }
  },
  {
    path: '/chat',
    name: 'chat',
    component: ChatView,
    meta: { requiresAuth: true }
  },
  {
    path: '/chat/:channelId',
    name: 'chat-channel',
    component: ChatView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // Load user from storage if not already loaded
  if (!userStore.currentUser) {
    userStore.loadUserFromStorage()
  }

  const requiresAuth = to.meta.requiresAuth
  const isAuthenticated = userStore.isAuthenticated

  if (requiresAuth && !isAuthenticated) {
    // Redirect to login if authentication is required
    next({ name: 'login' })
  } else if (to.name === 'login' && isAuthenticated) {
    // Redirect to chat if already authenticated
    next({ name: 'chat' })
  } else {
    next()
  }
})

export default router
