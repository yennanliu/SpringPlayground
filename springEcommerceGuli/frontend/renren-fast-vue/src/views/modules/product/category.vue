<!-- 
    https://youtu.be/QngyGaQXxz4?t=351
    https://github.com/zli78122/gulimall_renren-fast-vue/blob/master/src/views/modules/product/category.vue 
-->

<template>
  <el-tree
    :data="menus"
    :props="defaultProps"
    @node-click="handleNodeClick"
  ></el-tree>
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
    getMenu(){
        //consloe.log(data);
        this.$http({
          url: this.$http.adornUrl('/product/category/list/tree'),
          method: 'get',
        //   params: this.$http.adornParams({
        //     'page': this.pageIndex,
        //     'limit': this.pageSize,
        //     'roleName': this.dataForm.roleName
        //   })

        // ({data}) : can get attr in data, so we can call data.data below : https://youtu.be/KKFJPtW3730?t=588
        }).then(({data}) => {
            console.log("get data success : ", data.data)
            this.menus = data.data
        })
    }
  },
  created(){
    this.getMenu();
  }
};
</script>

<style></style>