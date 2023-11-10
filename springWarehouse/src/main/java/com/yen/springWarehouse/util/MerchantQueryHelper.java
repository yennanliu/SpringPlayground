package com.yen.springWarehouse.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantQueryHelper implements Serializable, BaseQueryHelper {

    private static final long serialVersionUID = -977205069L;

    /**
     * query with merchant name
     */
    private String qryMerchantName;

    /**
     * query with merchant city
     */
    private String qryMerchantCity;

    /**
     * query with merchant type
     */
    private String qryMerchantType;

}
