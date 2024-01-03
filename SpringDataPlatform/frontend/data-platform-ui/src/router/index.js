import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

// jar
import Jar from "../views/Jar/Jar.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },

  // jart
  {
    path: "/jars",
    name: "Jar",
    component: Jar,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
