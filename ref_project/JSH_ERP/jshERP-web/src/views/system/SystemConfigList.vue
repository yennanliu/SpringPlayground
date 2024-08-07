<template>
  <a-card :style="cardStyle" :bordered="false">
    <a-row :gutter="24">
      <a-col :md="12">
        <a-spin :spinning="confirmLoading">
          <a-form :form="form" id="systemConfigModal">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公司名称">
              <a-input placeholder="请输入公司名称" v-decorator.trim="[ 'companyName', validatorRules.companyName]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="联系人">
              <a-input placeholder="请输入联系人" v-decorator.trim="[ 'companyContacts' ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公司地址">
              <a-input placeholder="请输入公司地址" v-decorator.trim="[ 'companyAddress' ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公司电话">
              <a-input placeholder="请输入公司电话" v-decorator.trim="[ 'companyTel' ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公司传真">
              <a-input placeholder="请输入公司传真" v-decorator.trim="[ 'companyFax' ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公司邮编">
              <a-input placeholder="请输入公司邮编" v-decorator.trim="[ 'companyPostCode' ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="销售协议">
              <a-input placeholder="请输入销售协议" v-decorator.trim="[ 'saleAgreement', validatorRules.saleAgreement ]" />
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="仓库权限">
              <a-switch checked-children="启用" un-checked-children="关闭" v-model="depotFlagSwitch" @change="onDepotChange"></a-switch>
              （如果启用则需要到<b>用户管理</b>进行<b>分配仓库</b>）
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户权限">
              <a-switch checked-children="启用" un-checked-children="关闭" v-model="customerFlagSwitch" @change="onCustomerChange"></a-switch>
              （如果启用则需要到<b>用户管理</b>进行<b>分配客户</b>）
            </a-form-item>
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="负库存">
              <a-switch checked-children="启用" un-checked-children="关闭" v-model="minusStockFlagSwitch" @change="onMinusStockChange"></a-switch>
              （如果启用则单据支持负库存，批次商品除外）
            </a-form-item>
          </a-form>
        </a-spin>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :md="12" align="middle">
        <a-button type="primary" @click="handleOk">保存</a-button>
        <a-button style="margin-left:20px" @click="handleReset">重置</a-button>
      </a-col>
    </a-row>
  </a-card>
</template>
<!-- b y 7 5 2 7  1 8 9 2 0 -->
<script>
  import pick from 'lodash.pick'
  import {addSystemConfig,editSystemConfig,checkSystemConfig } from '@/api/api'
  import {autoJumpNextInput} from "@/utils/util"
  import {getAction } from '@/api/manage'
  import {mixinDevice} from '@/utils/mixin.js'
  export default {
    name: "SystemConfigList",
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
        cardStyle: '',
        visible: true,
        model: {},
        depotFlagSwitch: false, //仓库权限状态
        customerFlagSwitch: false, //客户权限状态
        minusStockFlagSwitch: false, //负库存状态
        isReadOnly: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          companyName:{
            rules: [
              { required: true, message: '请输入公司名称!' },
              { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
            ]
          },
          saleAgreement:{
            rules: [
              { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
            ]
          }
        }
      }
    },
    created () {
      this.init()
      if(this.isDesktop()) {
        this.cardStyle = 'height:' + (document.documentElement.clientHeight-125) + 'px'
      }
    },
    methods: {
      onDepotChange(checked) {
        this.model.depotFlag = checked?'1':'0'
      },
      onCustomerChange(checked) {
        this.model.customerFlag = checked?'1':'0'
      },
      onMinusStockChange(checked) {
        this.model.minusStockFlag = checked?'1':'0'
      },
      init () {
        let param = {
          search: {"companyName":""},
          currentPage: 1,
          pageSize: 10
        }
        getAction('/systemConfig/list', param).then((res)=>{
          if(res.code === 200){
            let record = res.data.rows[0]
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'companyName', 'companyContacts', 'companyAddress',
                'companyTel', 'companyFax', 'companyPostCode', 'saleAgreement', 'depotFlag', 'customerFlag', 'minusStockFlag'))
              autoJumpNextInput('systemConfigModal')
            });
            if(record.id) {
              if (record.depotFlag != null) {
                this.depotFlagSwitch = record.depotFlag == '1' ? true : false;
              }
              if (record.customerFlag != null) {
                this.customerFlagSwitch = record.customerFlag == '1' ? true : false;
              }
              if (record.minusStockFlag != null) {
                this.minusStockFlagSwitch = record.minusStockFlag == '1' ? true : false;
              }
            }
          } else {
            this.$message.info(res.data);
          }
        })
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            let obj;
            if(!this.model.id){
              obj=addSystemConfig(formData);
            }else{
              obj=editSystemConfig(formData);
            }
            obj.then((res)=>{
              if(res.code === 200){
                this.init()
                that.$message.info('保存成功！');
              }else{
                that.$message.warning(res.data.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
        })
      },
      handleReset () {
        this.form.resetFields();
        this.depotFlagSwitch = false
        this.customerFlagSwitch = false
        this.minusStockFlagSwitch = false
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>