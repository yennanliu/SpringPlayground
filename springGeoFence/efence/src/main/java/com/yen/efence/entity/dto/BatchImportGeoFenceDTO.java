package com.yen.efence.entity.dto;

// book p.4-25
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/dto/BatchImportGeoFenceDTO.java

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.yen.efence.entity.bo.GeoFenceBO;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class BatchImportGeoFenceDTO implements Serializable {

    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 管理归属分区编码
     */
    private String adCode;
    /**
     * 图层编码，管理图层信息
     */
    private String layerCode;
    /**
     * 电子围栏数据列表
     */
    @NotNull(message = "导入围栏数据不能为空")
    private List<GeoFenceBO> fences;

    /**
     * 批次导入ID
     */
    private Integer batchId;
}