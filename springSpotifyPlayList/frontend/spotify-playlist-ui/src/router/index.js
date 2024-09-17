import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

// album
import GetAlbum from "../views/Album/GetAlbum.vue";

// playlist
import CreatePlayList from "../views/PlayList/CreatePlayList.vue";

// profile
import UserPlayList from "../views/Profile/ListUserPlayList.vue";

// Recommendation
import GetRecommendation from "../views/Recommendation/GetRecommendation.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
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
    name: "UserPlayList",
    component: UserPlayList,
  },

  // Recommendation
  {
    path: "/recommendation",
    name: "GetRecommendation",
    component: GetRecommendation,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
