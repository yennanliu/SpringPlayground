package com.wudimanong.experiment.client.entity.bo;

import com.wudimanong.experiment.client.entity.AbtestExp;
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
public class ConfigBO {

    /**
     * 业务系统实验标签
     */
    private String factorTag;
    /**
     * 实验配置信息
     */
    private AbtestExp abtestExp;
}
