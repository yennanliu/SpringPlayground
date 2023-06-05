<template>
  <el-dialog
    :title="!dataForm.id ? 'Add' : 'Update'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @closed="dialogClose">
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px">
      <el-form-item label="Name" prop="attrGroupName">
        <el-input v-model="dataForm.attrGroupName" placeholder="Attribute Group Name"></el-input>
      </el-form-item>
      <el-form-item label="Sort" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="Sort"></el-input>
      </el-form-item>
      <el-form-item label="Desp" prop="descript">
        <el-input v-model="dataForm.descript" placeholder="Description"></el-input>
      </el-form-item>
      <el-form-item label="Icon" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="Icon"></el-input>
      </el-form-item>
      <el-form-item label="Category" prop="catelogId">
        <!-- 
          https://youtu.be/i3NZnXNTYBk?t=84 
          級聯選擇

          props setting:
          https://youtu.be/i3NZnXNTYBk?t=228
        -->
        <el-cascader
          style="width: 100%"
          filterable
          placeholder="Try to search: Phone"
          v-model="catelogPath"
          :options="categories"
          :props="props"
        ></el-cascader>
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
      categories: [],    //所有商品分类信息
      catelogPath: [],   //目标 AttrGroup对象 的 商品分类 的 catelogPath - 从祖先节点到自身 的路径
      // props setting : https://youtu.be/i3NZnXNTYBk?t=228
      props: {
        value: "catId",
        label: "name",
        children: "children"
      },
      visible: false,
      dataForm: {   //目标 AttrGroup对象
        attrGroupId: 0,
        attrGroupName: "",
        sort: "",
        descript: "",
        icon: "",
        catelogId: 0   //目标 AttrGroup对象 的 商品分类id
      },
      dataRule: {
        attrGroupName: [
          { required: true, message: "组名不能为空", trigger: "blur" }
        ],
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        descript: [
          { required: true, message: "描述不能为空", trigger: "blur" }
        ],
        icon: [{ required: true, message: "组图标不能为空", trigger: "blur" }],
        catelogId: [
          { required: true, message: "所属分类id不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getCategories();
  },
  methods: {
    // 关闭对话框
    dialogClose() {
      // 清空 catelogPath
      this.catelogPath = [];
    },

    // https://youtu.be/i3NZnXNTYBk?t=150
    getCategories() {
      // 查询所有商品分类信息
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get"
      }).then(({ data }) => {
        this.categories = data.data;
      });
    },

    init(id) {
      this.dataForm.attrGroupId = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.attrGroupId) {
          this.$http({
            url: this.$http.adornUrl(
              `/product/attrgroup/info/${this.dataForm.attrGroupId}`
            ),
            method: "get",
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.attrGroupName = data.attrGroup.attrGroupName;
              this.dataForm.sort = data.attrGroup.sort;
              this.dataForm.descript = data.attrGroup.descript;
              this.dataForm.icon = data.attrGroup.icon;
              this.dataForm.catelogId = data.attrGroup.catelogId;
              // 目标 AttrGroup对象 的 商品分类 的 catelogPath - 从祖先节点到自身 的路径
              this.catelogPath =  data.attrGroup.catelogPath;
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
              `/product/attrgroup/${
                !this.dataForm.attrGroupId ? "save" : "update"
              }`
            ),
            method: "post",
            data: this.$http.adornData({
              attrGroupId: this.dataForm.attrGroupId || undefined,
              attrGroupName: this.dataForm.attrGroupName,
              sort: this.dataForm.sort,
              descript: this.dataForm.descript,
              icon: this.dataForm.icon,
              // 目标 AttrGroup对象 的 商品分类id 为 商品分类路径 catelogPath数组 的最后一个元素
              catelogId: this.catelogPath[this.catelogPath.length - 1]
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList"); // https://youtu.be/i3NZnXNTYBk?t=672
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