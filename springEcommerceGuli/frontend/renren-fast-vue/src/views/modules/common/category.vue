<template>
  <div>
    <el-tree
      :data="menus"
      :props="defaultProps"
      node-key="catId"
      ref="menuTree"
      @node-click="nodeClick"
    ></el-tree>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      expandedKey: [],   //默认展开的节点
    };
  },
  computed: {},
  watch: {},
  methods: {
    // 节点被点击时的回调
    // 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身
    nodeClick(data, node, component) {
      // 向父组件发送 tree-node-click 事件
      this.$emit("tree-node-click", data, node, component);
    },

    getMenus() {
      // 查询所有商品分类信息
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get"
      }).then(({ data }) => {
        this.menus = data.data;
      });
    }
  },
  created() {
    this.getMenus();
  },
  mounted() {},
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {}
};
</script>

<style scoped>

</style>