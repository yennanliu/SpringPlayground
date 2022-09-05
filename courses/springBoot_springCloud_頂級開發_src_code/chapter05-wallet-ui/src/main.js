// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// 引入ElementUI
import ElementUI from 'element-ui'
// 引入ElementUI框架的样式文件
import 'element-ui/lib/theme-chalk/index.css'
// 引入axios
// eslint-disable-next-line no-unused-vars
import axios from 'axios'

// Vue使用ElementUI
Vue.use(ElementUI)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
