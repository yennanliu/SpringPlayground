package com.wudimanong.experiment.client.entity.bo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlowRatioBO {

    /**
     * 接入方应用名称
     */
    private String appName;

    /**
     * 实验标签
     */
    private String factorTag;

    /**
     * 调整后流量占比分配情况
     */
    private List<GroupFlowRatioBO> flowRatio;
}
