package com.wudimanong.efence.entity.dto;

import com.wudimanong.efence.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class FenceGeoLayerSaveDTO implements Serializable {

    /**
     * 图层编码
     */
    @NotNull(message = "图层编码不能为空")
    private String code;

    /**
     * 图层名称
     */
    @NotNull(message = "图层编码不能为空")
    private String name;

    /**
     * 图层业务类型：0-未知分类；1-干预；2-调度；3-停车围栏；4-运营范围；5-技术
     */
    @EnumValue(intValues = {0, 1, 2, 3, 4, 5}, message = "围栏类型输入有误")
    private Integer businessType;

    /**
     * 归属城市编码
     */
    @NotNull(message = "归属城市编码不能为空")
    private Integer cityCode;
    /**
     * 地理范围：0-全球；1-国内；2-海外
     */
    @EnumValue(intValues = {0, 1, 2}, message = "地理范围输入有误")
    private Integer regionType;

    /**
     * 详细说明
     */
    private String explain;

    /**
     * 数据负责人，格式为"名字+邮箱"
     */
    private String owner;
}
