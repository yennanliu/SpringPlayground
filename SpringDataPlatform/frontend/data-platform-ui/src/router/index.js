import Vue from "vue";
import VueRouter from "vue-router";
//import HomeView from "../views/HomeView.vue";
import Home from "../views/Home.vue";

// jar
import Jar from "../views/Jar/Jar.vue";
import ListJar from "../views/Jar/ListJar.vue";
import ShowJarDetails from "../views/Jar/ShowJarDetails.vue";
import AddJar from "../views/Jar/AddJar.vue";

// job
import Job from "../views/Job/Job.vue";
import ListJob from "../views/Job/ListJob.vue";
import ShowJobDetails from "../views/Job/ShowJobDetails.vue";
import AddJob from "../views/Job/AddJob.vue";

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
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },

  // jar
  {
    path: "/jar",
    name: "Jar",
    component: Jar,
  },
  {
    path: "/jars/",
    name: "ListJar",
    component: ListJar,
  },
  {
    path: "/jars/show/:id",
    name: "ShowJarDetails",
    component: ShowJarDetails,
  },
  {
    path: "/admin/jars/add",
    name: "AddJar",
    component: AddJar,
  },

  // job
  {
    path: "/job",
    name: "Job",
    component: Job,
  },
  {
    path: "/jobs/",
    name: "ListJob",
    component: ListJob,
  },
  {
    path: "/jobs/show/:id",
    name: "ShowJobDetails",
    component: ShowJobDetails,
  },
  {
    path: "/admin/jobs/add",
    name: "AddJob",
    component: AddJob,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;