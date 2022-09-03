package com.wudimanong.experiment.starter.entity;

import com.wudimanong.experiment.client.entity.AbtestExp;
import com.wudimanong.experiment.client.entity.AbtestGroup;
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
public class MatchResult {

    /**
     * 目标分桶结果
     */
    private AbtestGroup destGroup;

    /**
     * 原始实验配置信息
     */
    private AbtestExp abtestExp;

    /**
     * 匹配类型（有分桶编号、白名单两种类型）
     */
    private RetrieveType retrieveType;

    public enum RetrieveType {
        BUCKET, WHITE_LIST
    }
}



