package com.yen.gulimall.common.to.es;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 *  To model for ES
 *      - https://youtu.be/X-ToZ1RIH4A?t=215
 */

@Data
public class SkuEsModel {

    private Long SkuId;
    private Long SpuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catelogId;
    private String brandName;
    private String brandImg;
    private String catelogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

}
