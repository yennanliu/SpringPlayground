<template>
  <el-dialog
    :title="!dataForm.id ? 'Add' : 'Update'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px"
    >
      <el-form-item label="Session Name" prop="name">
        <el-input v-model="dataForm.name" placeholder="Session Name"></el-input>
      </el-form-item>
      <el-form-item label="Start Time" prop="startTime">
        <el-date-picker type="datetime" placeholder="Start Time" v-model="dataForm.startTime"></el-date-picker>
      </el-form-item>
      <el-form-item label="End Time" prop="endTime">
        <el-date-picker type="datetime" placeholder="End Time" v-model="dataForm.endTime"></el-date-picker>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-input v-model="dataForm.status" placeholder="Status"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Confirm</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        name: "",
        startTime: "",
        endTime: "",
        status: "",
        createTime: ""
      },
      dataRule: {
        name: [
          { required: true, message: "Session name can not be null", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "Start time can not be null", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "End time can not be null", trigger: "blur" }
        ],
        status: [
          { required: true, message: "Status can not be null", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(
              `/coupon/seckillsession/info/${this.dataForm.id}`
            ),
            method: "get",
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.seckillSession.name;
              this.dataForm.startTime = data.seckillSession.startTime;
              this.dataForm.endTime = data.seckillSession.endTime;
              this.dataForm.status = data.seckillSession.status;
              this.dataForm.createTime = data.seckillSession.createTime;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(
              `/coupon/seckillsession/${!this.dataForm.id ? "save" : "update"}`
            ),
            method: "post",
            data: this.$http.adornData({
              id: this.dataForm.id || undefined,
              name: this.dataForm.name,
              startTime: this.dataForm.startTime,
              endTime: this.dataForm.endTime,
              status: this.dataForm.status,
              createTime: new Date()
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                }
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    }
  }
};
</script>
