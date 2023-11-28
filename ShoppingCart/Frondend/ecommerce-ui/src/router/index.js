import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

import AddCategory from "../views/Category/AddCategory";
import EditCategory from "../views/Category/EditCategory";
import Category from "../views/Category/Category";
import Wishlist from "../views/Product/Wishlist.vue";
import ListProducts from '../views/Category/ListProducts.vue'

import Product from '../views/Product/Product.vue'
import ShowDetails from '../views/Product/ShowDetails.vue'

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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },

  // Category
  {
    path: "/admin/category/add",
    name: "AddCategory",
    component: AddCategory,
  },

  {
    path: "/admin/category",
    name: "AdminCategory",
    component: Category,
  },

  {
    path: "/admin/category/:id",
    name: "EditCategory",
    component: EditCategory,
  },

  {
    path: "/wishlist",
    name: "Wishlist",
    component: Wishlist,
  },
  {
    path : '/category/show/:id',
    name : 'ListProducts',
    component: ListProducts
  },

  //Product routes
  {
    path: '/product',
    name: 'Product',
    component: Product
  },
  {
    path: '/admin/product',
    name: 'AdminProduct',
    component: Product
  },
  // {
  //   path: '/admin/product/add',
  //   name: 'AddProduct',
  //   component: AddProduct
  // },
  // {
  //   path: '/admin/product/:id',
  //   name: 'EditProduct',
  //   component: EditProduct,
  // },
  {
    path : '/product/show/:id',
    name : 'ShowDetails',
    component: ShowDetails
  },


];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
