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
    }]
})
