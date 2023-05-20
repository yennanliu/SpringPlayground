<!-- 
    https://youtu.be/QngyGaQXxz4?t=351
    https://github.com/zli78122/gulimall_renren-fast-vue/blob/master/src/views/modules/product/category.vue 
-->

<template>
  <el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick">
    <!--
      Append, delete button

      https://youtu.be/-2S3c3Sh-H4?t=78
      https://element.eleme.io/#/zh-CN/component/tree
    -->
    <span class="custom-tree-node" slot-scope="{ node, data }">
      <span>{{ node.label }}</span>
      <span>
        <el-button type="text" size="mini" @click="() => append(data)">
          Append
        </el-button>
        <el-button type="text" size="mini" @click="() => remove(node, data)">
          Delete
        </el-button>
      </span>
    </span>
  </el-tree>
</template>

<script>
export default {
  components: {},
  props: {},
  computed: {},
  watch: {},
  data() {
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },

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
  },

  created() {
    this.getMenu();
  },

  // https://youtu.be/-2S3c3Sh-H4?t=138
  // https://element.eleme.io/#/zh-CN/component/tree
  append(data) {
    const newChild = { id: id++, label: "testtest", children: [] };
    if (!data.children) {
      this.$set(data, "children", []);
    }
    data.children.push(newChild);
  },

  remove(node, data) {
    const parent = node.parent;
    const children = parent.data.children || parent.data;
    const index = children.findIndex((d) => d.id === data.id);
    children.splice(index, 1);
  },
};
</script>

<style></style>