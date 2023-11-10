package com.yen.springWarehouse.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductQueryHelper implements Serializable, BaseQueryHelper {

    private static final long serialVersionUID = -9777777557205069L;

    /**
     * query with product name
     */
    private String qryProductName;

    /**
     * query with product min price
     */
    private Double qryStartPrice;

    /**
     * query with product max price
     */
    private Double qryEndPrice;

    /**
     * query with product name type
     */
    private String qryProductType;

}
