package com.wudimanong.experiment.client.entity;

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
public class AbtestExp {

    /**
     * 实验ID
     */
    private Integer expId;

    /**
     * 业务系统实验标签
     */
    private String factorTag;

    /**
     * 分层ID
     */
    private Integer layerId;

    /**
     * 分组类型ID，1表示按用户分组
     */
    private Integer groupFieldId;

    /**
     * 分区类型
     */
    private String partitionType;

    /**
     * 是否抽样打点
     */
    private Boolean isSampling;

    /**
     * 抽样率
     */
    private Integer samplingRatio;

    /**
     * 实验分组信息列表
     */
    private List<AbtestGroup> abtestGroups;

    /**
     * 实验配置
     */
    private String config;

}
