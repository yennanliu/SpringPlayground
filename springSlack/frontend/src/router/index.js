import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

// Views
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';
import ChannelView from '@/views/ChannelView.vue';
import DirectMessageView from '@/views/DirectMessageView.vue';
import NotFound from '@/views/NotFound.vue';

const routes = [
  {
    path: '/',
    redirect: '/channels/general'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresGuest: true }
  },
  {
    path: '/channels/:channelId',
    name: 'Channel',
    component: ChannelView,
    meta: { requiresAuth: true }
  },
  {
    path: '/dm/:userId',
    name: 'DirectMessage',
    component: DirectMessageView,
    meta: { requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navigation guards
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore();
  
  // Check if the user is authenticated
  if (!userStore.isAuthenticated) {
    await userStore.checkAuth();
  }
  
  // Handle routes that require authentication
  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    return next({ name: 'Login' });
  }
  
  // Handle routes that require guest access
  if (to.meta.requiresGuest && userStore.isAuthenticated) {
    return next({ path: '/' });
  }
  
  next();
});

export default router; 