package com.yen.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// https://youtu.be/2Fgtxnc9ehQ?t=921
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
