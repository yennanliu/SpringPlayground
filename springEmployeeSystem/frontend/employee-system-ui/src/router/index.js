import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

// user
import ListUsers from "../views/User/ListUsers.vue";
import ShowDetails from "../views/User/ShowDetails.vue";

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

  // user
  {
    path: "/users/",
    name: "ListUsers",
    component: ListUsers,
  },
  {
    path: "/users/show/:id",
    name: "ShowDetails",
    component: ShowDetails,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
