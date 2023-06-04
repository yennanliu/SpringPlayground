<template>
    <el-row :gutter="20">
      <el-col :span="6">
        <!-- 
            https://youtu.be/yf71dyduu_s?t=1276 
            
            -> listen tree-node-click event from child node (category.vue)
            -> then trigger its parent's method : treeNodeClick
        -->
        <category @tree-node-click="treeNodeClick"></category>
      </el-col>
      <el-col :span="18">
        <div class="mod-config">
          <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
            <el-form-item>
              <el-input v-model="dataForm.key" placeholder="Name" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="getDataList()">Search</el-button>
              <el-button type="success" @click="getAllDataList()">Search All</el-button>
              <el-button
                v-if="isAuth('product:attrgroup:save')"
                type="primary"
                @click="addOrUpdateHandle()"
              >Add</el-button>
              <el-button
                v-if="isAuth('product:attrgroup:delete')"
                type="danger"
                @click="deleteHandle()"
                :disabled="dataListSelections.length <= 0"
              >Batch Delete</el-button>
            </el-form-item>
          </el-form>
          <el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="selectionChangeHandle"
            style="width: 100%;">
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column prop="attrGroupId" header-align="center" align="center" label="Group Id"></el-table-column>
            <el-table-column prop="attrGroupName" header-align="center" align="center" label="Name"></el-table-column>
            <el-table-column prop="sort" header-align="center" align="center" label="Sort"></el-table-column>
            <el-table-column prop="descript" header-align="center" align="center" label="Desp"></el-table-column>
            <el-table-column prop="icon" header-align="center" align="center" label="Icon"></el-table-column>
            <el-table-column prop="catelogId" header-align="center" align="center" label="CategoryId"></el-table-column>
            <el-table-column
              fixed="right"
              header-align="center"
              align="center"
              width="150"
              label="Actions">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="relationHandle(scope.row.attrGroupId)">Related Attributes</el-button>
                <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.attrGroupId)">Update</el-button>
                <el-button type="text" size="small" @click="deleteHandle(scope.row.attrGroupId)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="totalPage"
            layout="total, sizes, prev, pager, next, jumper"
          ></el-pagination>
          <!-- 弹窗, 新增 / 修改 -->
          <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
          <!-- 弹窗, 关联 -->
          <relation-update v-if="relationVisible" ref="relationUpdate" @refreshData="getDataList"></relation-update>
        </div>
      </el-col>
    </el-row>
  </template>
  

  <script>
  // https://youtu.be/yf71dyduu_s?t=605
  /** 
   *  https://youtu.be/yf71dyduu_s?t=955
   * 
   *  - Parent, children nodes pass data
   *    - 1) children node pass data to parent -> event mechanisms
   *         - children node send event (with data) to parent
   *         - this.$emit("event_name", "data"....)
   */
  import Category from "../common/category";
  import AddOrUpdate from "./attrgroup-add-or-update";
  import RelationUpdate from "./attr-group-relation";
  
  export default {
    // NOTE : after import, have to register here, then we can use import components
    // syntax : import_componenets:name_to_use
    components: { Category:Category, AddOrUpdate:AddOrUpdate, RelationUpdate:RelationUpdate },
    props: {},
    data() {
      return {
        catId: 0,
        dataForm: {
          key: ""
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        relationVisible: false
      };
    },
    activated() {
      this.getDataList();
    },
    methods: {
      // 打开 关联 对话框
      relationHandle(groupId) {
        this.relationVisible = true;
        this.$nextTick(() => {
          this.$refs.relationUpdate.init(groupId);
        });
      },
  
      // 树节点被点击触发的回调 (category.vue nodeClick)
      treeNodeClick(data, node, component) {
        // https://youtu.be/10yPrgpSEG4?t=816
        if (node.level === 3) {
          console.log(">>> attrgroup know that category node was clicked : ", data, node, component);
          console.log(">>> Menu Id (category) was clicked : ", data.catId);
          this.catId = data.catId;
          this.getDataList(); //获取数据列表
        }
      },
  
      // 获取所有 属性分组(AttrGroup) 数据
      getAllDataList() {
        this.catId = 0;
        this.getDataList();
      },
  
      // 获取数据列表
      // https://youtu.be/10yPrgpSEG4?t=752
      getDataList() {
        this.dataListLoading = true;
        console.log(">>> this.catId = " + this.catId);
        this.$http({
          url: this.$http.adornUrl(`/product/attrgroup/list/${this.catId}`),
          method: "get",
          params: this.$http.adornParams({
            page: this.pageIndex,
            limit: this.pageSize,
            key: this.dataForm.key
          })
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list;
            this.totalPage = data.page.totalCount;
          } else {
            this.dataList = [];
            this.totalPage = 0;
          }
          this.dataListLoading = false;
        });
      },
      // 每页数
      sizeChangeHandle(val) {
        this.pageSize = val;
        this.pageIndex = 1;
        this.getDataList();
      },
      // 当前页
      currentChangeHandle(val) {
        this.pageIndex = val;
        this.getDataList();
      },
      // 多选
      selectionChangeHandle(val) {
        this.dataListSelections = val;
      },
      // 新增 / 修改
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true;
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id);
        });
      },
      // 删除
      deleteHandle(id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.attrGroupId;
        })
        this.$confirm(`确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/attrgroup/delete"),
            method: "post",
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.getDataList();
                }
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        });
      }
    }
  };
  </script>
  
  <style scoped>
  
  </style>