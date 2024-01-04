import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

// jar
import Jar from "../views/Jar/Jar.vue";
import ListJar from "../views/Jar/ListJar.vue";
import ShowJarDetails from "../views/Jar/ShowJarDetails.vue";

// job
import Job from "../views/Job/Job.vue";
import ListJob from "../views/Job/ListJob.vue";
import ShowJobDetails from "../views/Job/ShowJobDetails.vue";

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
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
