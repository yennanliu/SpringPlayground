package com.wudimanong.experiment.client.entity.dto;

import com.wudimanong.experiment.client.validator.EnumValue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class CreateExpDTO {

    /**
     * 接入方服务名称
     */
    @NotNull(message = "接入服务名不能为空")
    private String appName;

    /**
     * 实验业务标签
     */
    @NotNull(message = "实验业务标签不能为空")
    @Pattern(regexp = "[A-Za-z]\\w{4,34}_[0-9]{4}$", message = "实验标签不符合规范")
    private String factorTag;
    /**
     * 实验描述
     */
    @NotNull(message = "实验描述信息不能为空")
    private String desc;

    /**
     * 分组类型（1 用户，2 地理位置）
     */
    @EnumValue(intValues = {1, 2}, message = "分组类型不支持")
    private Integer groupField;

    /**
     * 流量层分层ID
     */
    private Integer layerId;

    /**
     * 实验负责人
     */
    private String owner;
}
