package com.wudimanong.efence.entity.bo;

import lombok.Data;

/**
 * @author jiangqiao
 */
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
