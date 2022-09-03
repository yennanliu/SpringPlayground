<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
// 引入axios
// eslint-disable-next-line no-unused-vars
import axios from 'axios'

export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.el-icon-cny{
    background: url(../src/assets/cny.png) center no-repeat;
    background-size: cover;
}
.el-icon-cny:before{
    content: "替";
    font-size: 35px;
    visibility: hidden;
}
</style>
