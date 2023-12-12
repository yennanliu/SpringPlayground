<template>
  <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link> |
    <router-link to="/category">Category</router-link> |
    <router-link to="/wishlist">Wishlist</router-link> |
    <router-link to="/product">Product</router-link> |
    <router-link to="/admin/category">Category (admin)</router-link> |
    <router-link to="/admin/wishlist">Wishlist (admin)</router-link> |
    <router-link to="/admin/product">Product (admin)</router-link> |
    <router-link to="/order">Order</router-link> |
    <router-link to="/cart">Cart</router-link> |
    <router-link to="/signup">Signup</router-link> |
    <router-link to="/signin">Signin</router-link>
  </nav>
</template>

<script>
export default {
  // You can add any logic specific to the navbar here
};
</script>

<style scoped>
/* Add your styling for the navbar here */
nav {
  background-color: #333;
  padding: 10px;
  color: white;
  text-align: center;
}
router-link {
  margin: 0 10px;
  color: white;
  text-decoration: none;
}
</style>
