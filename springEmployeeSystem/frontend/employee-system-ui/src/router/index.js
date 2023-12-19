import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

// user
import ListUsers from "../views/User/ListUsers.vue";
import ShowDetails from "../views/User/ShowDetails.vue";
import User from "../views/User/User.vue";
import EditUser from "../views/User/EditUser.vue";

// depearment
import ListDepartment from "../views/Department/ListDepartment.vue";
import ShowDepartmentDetails from "../views/Department/ShowDepartmentDetails.vue";
import Department from "../views/Department/Department.vue";
import EditDepartment from "../views/Department/EditDepartment.vue";

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
  {
    path: "/admin/users/:id",
    name: "EditUser",
    component: EditUser,
  },
  {
    path: "/admin/users",
    name: "AdminUser",
    component: User,
  },

  // depearment
  {
    path: "/departments/",
    name: "ListDepartment",
    component: ListDepartment,
  },
  {
    path: "/departments/show/:id",
    name: "ShowDepartmentDetails",
    component: ShowDepartmentDetails,
  },
  {
    path: "/admin/departments",
    name: "AdminDepartment",
    component: Department,
  },
  {
    path: "/admin/departments/:id",
    name: "EditDepartment",
    component: EditDepartment,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
