import Vue from 'vue'
import { createPinia, PiniaVuePlugin } from 'pinia'
import App from './App.vue'
import router from './router'

// Import global styles
import '@/styles/main.scss'

// Initialize validation
import '@/validation'

// Enable Pinia for Vue 2
Vue.use(PiniaVuePlugin)
const pinia = createPinia()

Vue.config.productionTip = false

new Vue({
  router,
  pinia,
  render: h => h(App)
}).$mount('#app')
