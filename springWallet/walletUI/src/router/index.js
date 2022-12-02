// book p.5-74
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet-ui/src/router/index.js

import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Charge from '@/components/Charge'
import QueryAcc from '@/components/QueryAcc'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'QueryAcc',
      component: QueryAcc
    },
    {
      path: '/charge',
      name: 'Charge',
      component: Charge
    }, {
      path: '/hello',
      name: 'HelloWorld',
      component: HelloWorld
    }
  ]
})
