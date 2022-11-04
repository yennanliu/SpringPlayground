package com.yen.efence.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/bo/BatchImportGeoFenceBO.java

import lombok.Data;

@Data
public class BatchImportGeoFenceBO {

    /**
     * 导入失败围栏在数据列表中的Index
     */
    private Integer index;

    /**
     * 导入失败原因
     */
    private String message;

}