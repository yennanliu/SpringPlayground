package com.wudimanong.efence.entity.dto;

import com.wudimanong.efence.entity.bo.GeoFenceBO;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
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
