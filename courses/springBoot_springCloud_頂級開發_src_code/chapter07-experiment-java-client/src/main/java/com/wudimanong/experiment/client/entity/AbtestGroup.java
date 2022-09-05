package com.wudimanong.experiment.client.entity;

import java.util.List;
import java.util.Set;
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
public class AbtestGroup {

    /**
     * 实验ID
     */
    private Integer expId;

    /**
     * 分组ID
     */
    private Integer groupId;

    /**
     * 分组类型，0-实验组；1-对照组
     */
    private Integer groupType;

    /**
     * 分组类型
     */
    private String groupPartitionType;

    /**
     * 原始桶
     */
    private Set<Integer> partitionSerialNums;

    /**
     * 桶是否经过压缩
     */
    private Boolean isUseBase64Nums;

    /**
     * 压缩后的桶
     */
    private String partitionSerialNums64;

    /**
     * 稀释倍率
     */
    private Integer dilutionRatio;

    /**
     * 策略信息
     */
    private List<Strategy> abtestStrategies;

    /**
     * 白名单
     */
    private List<String> whiteList;

}
