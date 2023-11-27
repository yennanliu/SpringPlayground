import Vue from 'vue'
import App from './App.vue'
import router from './router'

/** APP ENTRY POINT */

window.axios = require('axios')

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
