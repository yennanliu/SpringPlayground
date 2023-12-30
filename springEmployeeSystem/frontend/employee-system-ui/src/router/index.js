import Vue from "vue";
import VueRouter from "vue-router";
//import HomeView from "../views/HomeView.vue";
import Home from "../views/Home.vue";

// user
import ListUsers from "../views/User/ListUsers.vue";
import ShowUserDetails from "../views/User/ShowUserDetails.vue";
import User from "../views/User/User.vue";
import EditUser from "../views/User/EditUser.vue";

// depearment
import ListDepartment from "../views/Department/ListDepartment.vue";
import ShowDepartmentDetails from "../views/Department/ShowDepartmentDetails.vue";
import Department from "../views/Department/Department.vue";
import EditDepartment from "../views/Department/EditDepartment.vue";
import AddDepartment from "../views/Department/AddDepartment.vue";

// vacation
import Vacation from "../views/Vacation/Vacation.vue";
import AddVacation from "../views/Vacation/AddVacation.vue";

// ticket
import ListTickets from "../views/Ticket/ListTickets.vue";
import ShowTicketDetails from "../views/Ticket/ShowTicketDetails.vue";
import Ticket from "../views/Ticket/Ticket.vue";
import AddTicket from "../views/Ticket/AddTicket.vue";

// checkin
import Checkin from "../views/Checkin/Checkin.vue";

// signin, signup
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

// admin
import Admin from "../views/Admin/Admin.vue";

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
    name: "ShowUserDetails",
    component: ShowUserDetails,
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
  {
    path: "/admin/departments/add",
    name: "AddDepartment",
    component: AddDepartment,
  },

  // vacation
  {
    path: "/vacation",
    name: "Vacation",
    component: Vacation,
  },
  {
    path: "/admin/vacation/add",
    name: "AddVacation",
    component: AddVacation,
  },
  // ticket
  {
    path: "/ticket",
    name: "Ticket",
    component: Ticket,
  },
  {
    path: "/tickets/",
    name: "ListTickets",
    component: ListTickets,
  },
  {
    path: "/tickets/show/:id",
    name: "ShowTicketDetails",
    component: ShowTicketDetails,
  },
  {
    path: "/admin/tickets/add",
    name: "AddTicket",
    component: AddTicket,
  },
  {
    path: "/admin/tickets",
    name: "AdminTicket",
    component: Ticket,
  },

    // checkin
    {
      path: "/checkin",
      name: "Checkin",
      component: Checkin,
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
  //Admin routes
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
