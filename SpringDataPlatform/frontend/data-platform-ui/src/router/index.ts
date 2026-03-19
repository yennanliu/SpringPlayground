import { createRouter, createWebHistory, RouteRecordRaw, RouteLocationNormalized, NavigationGuardNext } from "vue-router";
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

// cluster
import Cluster from "../views/Cluster/Cluster.vue";
import ListCluster from "../views/Cluster/ListCluster.vue";
import ShowClusterDetails from "../views/Cluster/ShowClusterDetails.vue";
import AddCluster from "../views/Cluster/AddCluster.vue";

// SqlJob
import AddSqlJob from "../views/SqlJob/AddSqlJob.vue";

// Zeppelin
import Zeppelin from "../views/Zeppelin/Zeppelin.vue";
import ZeppelinApp from "../views/Zeppelin/ZeppelinApp.vue";
import ListNotebook from "../views/Zeppelin/ListNotebook.vue";
import AddNotebook from "../views/Zeppelin/AddNotebook.vue";
import NotebookConsole from "../views/Zeppelin/NotebookConsole.vue";

// signin, signup
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

// admin
import Admin from "../views/Admin/Admin.vue";

// auth store
import { useAuthStore } from "@/stores";

// Extend route meta type
declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
    guestOnly?: boolean
  }
}

const routes: RouteRecordRaw[] = [
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
    meta: { requiresAuth: true },
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
    meta: { requiresAuth: true },
  },

  // SqlJob
  {
    path: "/admin/sql_jobs/add",
    name: "AddSqlJob",
    component: AddSqlJob,
    meta: { requiresAuth: true },
  },

  // Zeppelin
  {
    path: "/zeppelin",
    name: "Zeppelin",
    component: Zeppelin,
  },
  {
    path: "/zeppelinApp",
    name: "ZeppelinApp",
    component: ZeppelinApp,
  },
  {
    path: "/notebooks",
    name: "ListNotebook",
    component: ListNotebook,
  },
  {
    path: "/admin/notebook/add",
    name: "AddNotebook",
    component: AddNotebook,
    meta: { requiresAuth: true },
  },
  {
    path: "/notebook_console",
    name: "NotebookConsole",
    component: NotebookConsole,
  },

  // Signin and Signup
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
    meta: { guestOnly: true },
  },
  {
    path: "/signin",
    name: "Signin",
    component: Signin,
    meta: { guestOnly: true },
  },

  // cluster
  {
    path: "/cluster",
    name: "Cluster",
    component: Cluster,
  },
  {
    path: "/clusters",
    name: "ListCluster",
    component: ListCluster,
  },
  {
    path: "/clusters/show/:id",
    name: "ShowClusterDetails",
    component: ShowClusterDetails,
  },
  {
    path: "/admin/clusters/add",
    name: "AddCluster",
    component: AddCluster,
    meta: { requiresAuth: true },
  },

  // Admin routes
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// Navigation guard
router.beforeEach((to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.checkAuth();

  // Check if route requires authentication
  if (to.meta.requiresAuth && !isAuthenticated) {
    // Redirect to signin with return URL
    next({
      name: "Signin",
      query: { redirect: to.fullPath },
    });
    return;
  }

  // Check if route is for guests only (signin/signup)
  if (to.meta.guestOnly && isAuthenticated) {
    // Redirect authenticated users to home
    next({ name: "Home" });
    return;
  }

  next();
});

export default router;
