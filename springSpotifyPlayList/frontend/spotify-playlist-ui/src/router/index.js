import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

// album
import GetAlbum from "../views/Album/GetAlbum.vue"; 

// playlist
import CreatePlayList from "../views/PlayList/CreatePlayList.vue"; 

// profile
import GetProfile from "../views/Profile/GetProfile.vue"; 

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },

  // album
  {
    path: "/album",
    name: "GetAlbum",
    component: GetAlbum,
  },

  // PlayList
  {
    path: "/playlist",
    name: "CreatePlayList",
    component: CreatePlayList,
  },

    // Profile
    {
      path: "/profile",
      name: "GetProfile",
      component: GetProfile,
    },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
