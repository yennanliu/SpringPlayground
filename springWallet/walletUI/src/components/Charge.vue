<!-- https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet-ui/src/components/Charge.vue -->
<template>
  <div id="charge">
    <el-card class="box-card">
      <ul class="msg-box">
        <li>
          <h4>钱包充值</h4>
        </li>
        <li>
          <h4 style="margin-bottom: 15px">充值金额</h4>
          <el-radio-group v-model="amountVal" @change="amountChange">
            <el-radio border :label="'' + 20">¥20</el-radio>
            <el-radio border :label="'' + 50">¥50</el-radio>
            <el-radio border :label="'' + 100">¥100</el-radio>
            <el-radio border :label="'' + 200">¥200</el-radio>
            <el-radio border :label="''">自定义</el-radio>
          </el-radio-group>
        </li>
        <li>
          <h4 style="margin-bottom: 15px">支付方式</h4>
          <el-radio-group
            v-model="rechargeParams.paymentType"
            @change="paymentTypeChange"
          >
            <el-radio border :label="'' + 0">微信</el-radio>
            <el-radio border :label="'' + 1">支付宝</el-radio>
          </el-radio-group>
        </li>
        <li>
          <h4 style="margin-bottom: 15px">充值金额</h4>
          <el-input
            :disabled="disabled"
            clearable
            v-model="rechargeParams.amount"
            placeholder="请输入金额"
            style="width: 150px"
          ></el-input>
        </li>
      </ul>
      <div style="text-align: center; margin-top: 30px">
        <el-button type="primary" @click="surePay">确认支付</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
// 引入axios
// eslint-disable-next-line no-unused-vars
import axios from 'axios';
export default {
  data () {
    return {
      amountVal: '',
      disabled: false,
      // 充值交易参数
      rechargeParams: {
        // eslint-disable-next-line no-undef
        userId: this.$route.query.userId, // 获取用户ID
        amount: '', // 金额
        currency: 'CNY', // 币种
        paymentType: '1', // 支付方式[0:微信,1:支付宝]，暂支持支付宝
        isRenew: '0', // 是否自动续费[0:否,1:是]
      },
    };
  },
  methods: {
    // 充值金额选择事件联动函数
    amountChange: function (val) {
      // 后端接口金额以分为单位
      this.rechargeParams.amount = val
      // eslint-disable-next-line eqeqeq
      if (val == '') {
        this.disabled = false
      } else {
        this.disabled = true
      }
    },
    // 支付方式选择事件联动函数
    paymentTypeChange: function (val) {
      this.rechargeParams.paymentType = val
    },
    // 确认支付按钮事件触发函数
    async surePay() {
      // eslint-disable-next-line eqeqeq
      if (this.rechargeParams.amount == "") {
        this.$message.warning("请输入金额!");
        return
      }
      // 调用钱包交易接口服务
      const res = await axios.post(
        '/api/account/chargeOrder',
        this.rechargeParams
      )
      const {
        code,
        // eslint-disable-next-line no-unused-vars
        message,
        // eslint-disable-next-line no-unused-vars
        data
      } = res.data
      if (code === 0) {
        // 支付方式跳转
        // eslint-disable-next-line eqeqeq
        if (this.rechargeParams.paymentType == '0') {
          // 暂时不支持微信
          this.$message.success('微信支付')
          this.wechatPay(data)
          // eslint-disable-next-line eqeqeq
        } else if (this.rechargeParams.paymentType == '1') {
          // 支持支付宝电脑网页支付
          this.$message.success('支付宝支付')
          const payDiv = document.getElementById('payDiv')
          if (payDiv) {
            document.body.removeChild(payDiv)
          }
          const div = document.createElement('div')
          div.id = 'payDiv'
          div.innerHTML = data.extraInfo // 返回的form表单数据
          document.body.appendChild(div)
          document
            .getElementById('payDiv')
            .getElementsByTagName('form')[0]
            .submit()
        }
      } else if (code === 401) {
        this.$message.error(message)
        this.$router.push({
          name: 'login'
        })
      } else {
        this.$message.error(message)
      }
    },
    // 微信支付(暂不支持)
    wechatPay (result) {
      if (result) {
        const orderParams = JSON.parse(result);
        sessionStorage.qrurl = orderParams.qrurl
        sessionStorage.amt = orderParams.amt
        sessionStorage.returnUrl = orderParams.returnUrl
        sessionStorage.order_id = orderParams.order_id
        this.$router.push({
          name: 'wechatPay'
        })
      }
    }
  }
}
</script>

<style scoped>
#charge {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: left;
  color: #2c3e50;
  margin-top: 0px;
}

/* 信息列表样式 */
.msg-box > li {
  list-style: none;
  border-bottom: 1px solid #c5c5c5;
  padding: 20px 10px;
}
</style>
