package com.wudimanong.experiment.service;

import com.wudimanong.experiment.client.entity.bo.ConfigBO;

/**
 * @author jiangqiao
 */
public interface AbtestDeliverService {

    /**
     * 根据业务标签获取实验配置信息
     *
     * @param factorTag
     * @return
     */
    ConfigBO getExpInfoByFactorTag(String factorTag);

}
