package com.wudimanong.experiment.client.entity.dto;

import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class UpdateFlowRatioDTO {

    /**
     * 应用名称（主要用于接入方身份标识）
     */
    @NotNull(message = "应用名称不能为空")
    private String appName;

    /**
     * 实验标签
     */
    @NotNull(message = "实验标签不能为空")
    private String factorTag;

    /**
     * 实验流量配比（example->{"76":20；"77":40）,76,77分别表示分组ID
     */
    private Map<Integer, Integer> flowRatioParam;
}
