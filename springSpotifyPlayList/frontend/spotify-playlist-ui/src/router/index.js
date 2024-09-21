import Vue from "vue";
import VueRouter from "vue-router";
//import HomeView from "../views/HomeView.vue";
import Home from "../views/Home.vue";

// album
import GetAlbum from "../views/Album/GetAlbum.vue";

// playlist
import CreatePlayList from "../views/PlayList/CreatePlayList.vue";

// profile
import UserPlayList from "../views/Profile/ListUserPlayList.vue";

// Recommendation
import GetRecommendation from "../views/Recommendation/GetRecommendation.vue";

// search
import SearchAlbum from "../views/Search/SearchAlbum.vue";
import SearchArtist from "../views/Search/SearchArtist.vue";

Vue.use(VueRouter);

const routes = [
  // {
  //   path: "/",
  //   name: "home",
  //   component: HomeView,
  // },
  {
    path: "/",
    name: "Home",
    component: Home,
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

  // search album
  {
    path: "/search_album",
    name: "SearchAlbum",
    component: SearchAlbum,
  },

  // search artist
  {
    path: "/search_artist",
    name: "SearchArtist",
    component: SearchArtist,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
