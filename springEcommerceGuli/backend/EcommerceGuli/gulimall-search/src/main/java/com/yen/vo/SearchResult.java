package com.yen.vo;

import com.yen.gulimall.common.to.es.SkuEsModel;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResult {

    // 查询到的所有商品信息
    private List<SkuEsModel> products;

    // 分页信息
    private Integer pageNum;        //当前页码
    private Long total;             //总记录数
    private Integer totalPages;     //总页码
    private List<Integer> pageNavs; //页码集合 - [1, 2, 3, ... totalPages]

    // 当前查询到的结果所涉及到的所有品牌
    private List<BrandVo> brands;

    // 当前查询到的结果所涉及到的所有分类
    private List<CatalogVo> catalogs;

    // 当前查询到的结果所涉及到的所有属性
    private List<AttrVo> attrs;

    // 面包屑导航数据
    private List<NavVo> navs = new ArrayList<>();

    // 已选择的所有属性 - 请求参数中包含的所有属性
    private List<Long> attrIds = new ArrayList<>();

    /**
     * 面包屑导航数据
     */
    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    /**
     * 品牌信息
     */
    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    /**
     * 分类信息
     */
    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    /**
     * 属性信息
     */
    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
