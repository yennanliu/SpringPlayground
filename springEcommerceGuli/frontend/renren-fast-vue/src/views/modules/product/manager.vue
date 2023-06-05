<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="Category">
          <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
        </el-form-item>
        <el-form-item label="Brand">
          <brand-select style="width:160px"></brand-select>
        </el-form-item>
        <el-form-item label="Prices">
          <el-input-number style="width:160px" v-model="dataForm.price.min" :min="0"></el-input-number>-
          <el-input-number style="width:160px" v-model="dataForm.price.max" :min="0"></el-input-number>
        </el-form-item>
        <!-- <el-form-item label="检索">
          <el-input style="width:160px" v-model="dataForm.key" clearable></el-input>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="searchSkuInfo">Search</el-button>
        </el-form-item>
      </el-form>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;"
      @expand-change="getSkuDetails"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          商品标题：{{scope.row.skuTitle}}
          <br />
          商品副标题：{{scope.row.skuSubtitle}}
          <br />
          商品描述：{{scope.row.skuDesc}}
          <br />
          分类ID：{{scope.row.catalogId}}
          <br />
          SpuID：{{scope.row.spuId}}
          <br />
          品牌ID：{{scope.row.brandId}}
          <br />
        </template>
      </el-table-column>
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="skuId" header-align="center" align="center" label="SKU Id"></el-table-column>
      <el-table-column prop="skuName" header-align="center" align="center" label="Name"></el-table-column>
      <el-table-column prop="skuDefaultImg" header-align="center" align="center" label="Default Image">
        <template slot-scope="scope">
          <img :src="scope.row.skuDefaultImg" style="width:80px;height:80px;" />
        </template>
      </el-table-column>
      <el-table-column prop="price" header-align="center" align="center" label="Price"></el-table-column>
      <el-table-column prop="saleCount" header-align="center" align="center" label="Sales"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="250" label="Actions">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="previewHandle(scope.row.skuId)">Preview</el-button>
          <el-button type="text" size="small" @click="commentHandle(scope.row.skuId)">Comments</el-button>
          <el-dropdown
            @command="handleCommand(scope.row,$event)"
            size="small"
            split-button
            type="text"
          >
            More
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="uploadImages">上传图片</el-dropdown-item>
              <el-dropdown-item command="seckillSettings">参与秒杀</el-dropdown-item>
              <el-dropdown-item command="reductionSettings">满减设置</el-dropdown-item>
              <el-dropdown-item command="discountSettings">折扣设置</el-dropdown-item>
              <el-dropdown-item command="memberPriceSettings">会员价格</el-dropdown-item>
              <el-dropdown-item command="stockSettings">库存管理</el-dropdown-item>
              <el-dropdown-item command="couponSettings">优惠劵</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
  </div>
</template>

<script>
import CategoryCascader from "../common/category-cascader";
import BrandSelect from "../common/brand-select";
export default {
  data() {
    return {
      catPathSub: null,
      brandIdSub: null,
      dataForm: {
        key: "",
        brandId: 0,
        catelogId: 0,
        price: {
          min: 0,
          max: 0
        }
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      catelogPath: []
    };
  },
  components: {
    CategoryCascader,
    BrandSelect
  },
  activated() {
    this.getDataList();
  },
  methods: {
    getSkuDetails(row, expand) {
      //sku详情查询
      console.log("展开某行...", row, expand);
    },
    //处理更多指令
    handleCommand(row, command) {
      console.log("~~~~~", row, command);
      if ("stockSettings" == command) {
        this.$router.push({ path: "/ware-sku", query: { skuId: row.skuId } });
      }
    },
    searchSkuInfo() {
      this.getDataList();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/product/skuinfo/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          key: this.dataForm.key,
          catelogId: this.dataForm.catelogId,
          brandId: this.dataForm.brandId,
          min: this.dataForm.price.min,
          max: this.dataForm.price.max
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
    }
  },
  mounted() {
    this.catPathSub = PubSub.subscribe("catPath", (msg, val) => {
      this.dataForm.catelogId = val[val.length - 1];
    });
    this.brandIdSub = PubSub.subscribe("brandId", (msg, val) => {
      this.dataForm.brandId = val;
    });
  },
  beforeDestroy() {
    PubSub.unsubscribe(this.catPathSub);
    PubSub.unsubscribe(this.brandIdSub);
  } //生命周期 - 销毁之前
};
</script>
