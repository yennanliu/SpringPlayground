import Vue from "vue";
import VueRouter from "vue-router";
//import HomeView from "../views/HomeView.vue";
import Home from "../views/Home.vue";

// Category
import Category from "../views/Category/Category.vue";
import AddCategory from "../views/Category/AddCategory.vue";
import EditCategory from "../views/Category/EditCategory.vue";

// Wishlist
import Wishlist from "../views/Product/Wishlist.vue";

// product
import ListProducts from "../views/Category/ListProducts.vue";
import AddProduct from "../views/Product/AddProduct.vue";
import EditProduct from "../views/Product/EditProduct.vue";
import Product from "../views/Product/Product.vue";
import ShowDetails from "../views/Product/ShowDetails.vue";

// Cart
//import Cart from "../views/Cart/Cart.vue";

// Checkout
import Checkout from "../views/Checkout/Checkout.vue";

// signin, signup
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

Vue.use(VueRouter);

const routes = [
  // home view
  {
    path: "/",
    name: "Home",
    component: Home,
  },

  // admin view

  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },

  //Category routes
  {
    path: "/category",
    name: "Category",
    component: Category,
  },
  {
    path: "/admin/category",
    name: "AdminCategory",
    component: Category,
  },
  {
    path: "/admin/category/add",
    name: "AddCategory",
    component: AddCategory,
  },
  {
    path: "/admin/category/:id",
    name: "EditCategory",
    component: EditCategory,
  },
  {
    path: "/category/show/:id",
    name: "ListProducts",
    component: ListProducts,
  },

  // wishlist
  {
    path: "/wishlist",
    name: "Wishlist",
    component: Wishlist,
  },

  //Product routes
  {
    path: "/product",
    name: "Product",
    component: Product,
  },
  {
    path: "/admin/product",
    name: "AdminProduct",
    component: Product,
  },
  {
    path: "/admin/product/add",
    name: "AddProduct",
    component: AddProduct,
  },
  {
    path: "/admin/product/:id",
    name: "EditProduct",
    component: EditProduct,
  },
  {
    path: "/product/show/:id",
    name: "ShowDetails",
    component: ShowDetails,
  },

  // Cart
  // {
  //   path: "/cart",
  //   name: "Cart",
  //   component: Cart,
  // },

  // checkout
  {
    path: "/checkout",
    name: "Checkout",
    component: Checkout,
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
