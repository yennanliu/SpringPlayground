<!-- 
    https://youtu.be/QngyGaQXxz4?t=351
    https://github.com/zli78122/gulimall_renren-fast-vue/blob/master/src/views/modules/product/category.vue 
-->

<template>
  <div>
    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandedKey"
    >
      <!--
      Append, delete button

      https://youtu.be/-2S3c3Sh-H4?t=78
      https://element.eleme.io/#/zh-CN/component/tree
    -->
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            Append
          </el-button>

          <!-- 
            Edit
            https://youtu.be/heF-gu9EXDs?t=43
          -->
          <el-button type="text" size="mini" @click="() => edit(data)">
            Edit
          </el-button>

          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>

    <!--
      dialogue
      https://youtu.be/KKv81DvbbMQ?t=105 

      :title="提示"
      bind title with variable
      https://youtu.be/heF-gu9EXDs?t=441
    -->
    <el-dialog v-bind:title="title" :visible.sync="dialogVisible" width="30%">
      <el-form :model="category">
        <el-form-item label="分類名稱">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <!-- https://youtu.be/KKv81DvbbMQ?t=356 -->
        <el-button type="primary" @click="submitData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { timeStamp } from "console";

export default {
  components: {},
  props: {},
  computed: {},
  watch: {},
  data() {
    return {
      title: "",
      dialogType: "", // edit, append ...
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
      },
      dialogVisible: false,
      menus: [],
      expandedKey: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  methods: {
    // https://youtu.be/QngyGaQXxz4?t=560
    getMenu() {
      //consloe.log(data);
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
        //   params: this.$http.adornParams({
        //     'page': this.pageIndex,
        //     'limit': this.pageSize,
        //     'roleName': this.dataForm.roleName
        //   })

        // ({data}) : can get attr in data, so we can call data.data below : https://youtu.be/KKFJPtW3730?t=588
      }).then(({ data }) => {
        console.log("get data success : ", data.data);
        this.menus = data.data;
      });
    },

    // https://youtu.be/-2S3c3Sh-H4?t=138
    // https://element.eleme.io/#/zh-CN/component/tree
    // https://youtu.be/KKv81DvbbMQ?t=140
    append(data) {
      console.log("append : ", data);
      this.dialogType = "append";
      (this.title = "Add category"), (this.dialogVisible = true);
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1; // transform string to int : data.catLevel * 1
    },

    // method does "product adding" in menu
    // https://youtu.be/KKv81DvbbMQ?t=375
    addCategory() {
      console.log("addCategory : ", this.category);
      // https://youtu.be/KKv81DvbbMQ?t=740
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(({ data }) => {
        // success popup
        this.$message({
          message: "add category success",
          type: "success",
        });
        // close dialogue
        this.dialogVisible = false;
        // retrieve updated product data from backend, so user can see updated info in UI without manually refresh
        this.getMenu();
        // expand deleted node's parent as new expanded node
        this.expandedKey = [this.category.parentCid]; // https://youtu.be/KKv81DvbbMQ?t=880
      });
    },

    // Edit category method
    // https://youtu.be/heF-gu9EXDs?t=66
    edit(data) {
      console.log("to edit : ", data);
      this.dialogType = "edit";
      (this.title = "Edit category"), (this.dialogVisible = true);
      this.category.name = data.name;
      this.category.catId = data.catId;
    },

    // modify category data
    editCategory() {},

    // method handles which dialogue to submit
    // https://youtu.be/heF-gu9EXDs?t=338
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },

    remove(node, data) {
      console.log("remove : ", node, data);
      var ids = [data.catId];

      // message box pop up
      // https://youtu.be/DTyZDng9nw0?t=599
      // https://element.eleme.io/#/zh-CN/component/message-box
      this.$confirm(`Delete current product : [${data.name}] ?`, "NOTE", {
        confirmButtonText: "YES",
        cancelButtonText: "NO",
        type: "warning",
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(ids, false),
          }).then(({ data }) => {
            console.log("remove success!");
            // message popup : https://youtu.be/DTyZDng9nw0?t=783
            this.$message({
              message: "menun remove success",
              type: "success",
            });
            // retrieve updated product data from backend, so user can see updated info in UI without manually refresh
            this.getMenu();
            // expand deleted node's parent as new expanded node
            this.expandedKey = [node.parent.data.catId]; // https://youtu.be/DTyZDng9nw0?t=1011
          });
        })
        .catch(() => {});
    },
  },

  created() {
    this.getMenu();
  },
};
</script>

<style></style>