<!-- 
    https://youtu.be/QngyGaQXxz4?t=351
    https://github.com/zli78122/gulimall_renren-fast-vue/blob/master/src/views/modules/product/category.vue 
-->

<template>
  <el-tree
    :data="menus"
    :props="defaultProps"
    :expand-on-click-node="false"
    show-checkbox
    node-key="catId"
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
    append(data) {
      console.log("append : ", data);
    },

    remove(node, data) {
      console.log("remove : ", node, data);
      var ids = [data.catId]
      this.$http({
        url: this.$http.adornUrl("/product/category/delete"),
        method: "post",
        data: this.$http.adornData(ids, false),
      }).then(({ data }) => {
        console.log("remove success!")
      });
    },
  },

  created() {
    this.getMenu();
  },

};
</script>

<style></style>