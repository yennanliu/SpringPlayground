package com.wudimanong.experiment.client.entity.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupFlowRatioBO implements Serializable {

    /**
     * 分组ID
     */
    private Integer groupId;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 调整后流量占比
     */
    private Integer flowRatio;

    /**
     * 调整前流量占比
     */
    private Integer preFlowRatio;
}
