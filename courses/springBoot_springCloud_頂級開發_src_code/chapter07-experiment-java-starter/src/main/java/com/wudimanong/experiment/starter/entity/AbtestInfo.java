package com.wudimanong.experiment.starter.entity;

import com.wudimanong.experiment.client.entity.Strategy;
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
@AllArgsConstructor
@NoArgsConstructor
public class AbtestInfo {

    /**
     * 实验Tag
     */
    private String factorTag;
    /**
     * 分流参数
     */
    private String paramId;

    /**
     * 匹配结果
     */
    private MatchResult result;

    /**
     * 策略信息
     */
    private List<Strategy> abtestStrategies;

    /**
     * 判断匹配分组是否为对照组
     *
     * @return
     */
    public Boolean isControl() {
        return this.result.getDestGroup().getGroupType().equals(1);
    }

    /**
     * 判断匹配分组是否为实验组
     *
     * @return
     */
    public Boolean isAbtest() {
        return this.result.getDestGroup().getGroupType().equals(1);
    }
}
