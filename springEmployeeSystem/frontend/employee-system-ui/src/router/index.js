import Vue from "vue";
import VueRouter from "vue-router";
//import HomeView from "../views/HomeView.vue";
import Home from "../views/Home.vue";

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

// signin, signup
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

Vue.use(VueRouter);

const routes = [
  // home view
  // {
  //   path: "/",
  //   name: "home",
  //   component: HomeView,
  // },
  // home view
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

  // user
  {
    path: "/user",
    name: "User",
    component: User,
  },
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
    path: "/departments",
    name: "Department",
    component: Department,
  },
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
  //Signin and Signup
  {
    path: "/signup",
    name: "Signup",
    component: Signup,
  },
  {
    path: "/signin",
    name: "Signin",
    component: Signin,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
