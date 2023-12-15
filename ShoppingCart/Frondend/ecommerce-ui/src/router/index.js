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
import Cart from "../views/Cart/Cart.vue";

// Checkout
import Checkout from "../views/Checkout/Checkout.vue";

// signin, signup
import Signup from "../views/Signup.vue";
import Signin from "../views/Signin.vue";

// success, failed
import Success from "../helper/payment/Success.vue";
import Failed from "../helper/payment/Failed.vue";

// orders
import Order from "../views/Orders/Order.vue";
import OrderDetails from "../views/Orders/OrderDetails";

// admin
import Admin from "../views/Admin/Admin.vue";
// import Gallery from '../views/Admin/Gallery.vue'
// import AddImage from '../views/Admin/AddImage.vue'

// user
import User from "../views/User/User.vue";
import ShowUserDetails from "../views/User/ShowUserDetails.vue";
import EditUser from "../views/User/EditUser.vue";

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
  {
    path: "/category/show/:id",
    name: "ListProducts",
    component: ListProducts,
  },

  // Cart
  {
    path: "/cart",
    name: "Cart",
    component: Cart,
  },

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

  // success, failed
  {
    path: "/payment/success",
    name: "PaymentSuccess",
    component: Success,
  },
  {
    path: "/payment/failed",
    name: "FailedPayment",
    component: Failed,
  },

  // orders
  {
    path: "/order",
    name: "Order",
    component: Order,
  },

  {
    path: "/order/:id",
    name: "OrderDetails",
    component: OrderDetails,
  },

  //Admin routes
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
  },

  //User routes
  {
    path: "/user",
    name: "User",
    component: User,
  },
  {
    path: "/admin/user/:id",
    name: "EditUser",
    component: EditUser,
  },
  {
    path: "/user/show/:id",
    name: "ShowUserDetails",
    component: ShowUserDetails,
  },
  {
    path: "/admin/user",
    name: "AdminUser",
    component: User,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
