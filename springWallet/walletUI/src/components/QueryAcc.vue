<!-- book p. 5-71 -->
<!-- https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet-ui/src/components/QueryAcc.vue -->
<template>
  <div id='queryAcc'>
    <!-- 由于Element-UI官方支持的ICON图标比较少，这里我们自定义一个货币图标-->
    <i class='el-icon-cny' /><br />
    <div>
      <span>账户余额</span>
    </div>
    <br />
    <!--调用后端余额查询接口进行数据渲染-->
    <div>
      {{ balance }}
    </div>
    <!--使用Element-UI组件添加充值按钮-->
    <br />
    <el-row>
      <el-button type='info' @click='toCharge'>充值</el-button>
    </el-row>
    <router-view />
  </div>
</template>
<script>
// 引入axios
// eslint-disable-next-line no-unused-vars
import axios from 'axios'

export default {
  name: 'App',
  // 页面数据定义
  data () {
    return {
      balance: ''
    }
  },
  // 在vue的created生命周期中实现向后端微服务查询余额的功能
  created () {
    this.getBalance()
  },
  methods: {
    // 获取用户余额方法
    getBalance: function () {
      // 调用钱包微服务账户查询接口，查询余额信息，这里的userId为开户时设置，真实环境通过会话动态获取
      axios.get('/api/account/queryAcc?userId=10001&accType=0').then(
        (response) => {
          // 通过接口返回数据为显示变量赋值
          this.balance = '¥' + response.data.data[0].balance / 100 + '元'
          console.log(response.data)
        },
        (response) => {
          console.log('error')
        }
      )
    },
    // 通过充值按钮点击跳转到钱包充值界面
    toCharge: function () {
      // 路由打开到充值界面，这里以重新打开新窗口的形式进行页面跳转
      let routeData = this.$router.resolve({
        path: '/charge',
        query: { userId: 10001 }
      })
      window.open(routeData.href, '_blank')
    }
  }
}
</script>

<style>
#queryAcc {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.el-icon-cny {
  background: url(../../src/assets/cny.png) center no-repeat;
  background-size: cover;
}
.el-icon-cny:before {
  content: '替';
  font-size: 35px;
  visibility: hidden;
}
</style>
