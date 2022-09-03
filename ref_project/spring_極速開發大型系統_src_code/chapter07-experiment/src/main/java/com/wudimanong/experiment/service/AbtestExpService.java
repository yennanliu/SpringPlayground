package com.wudimanong.experiment.service;

import com.wudimanong.experiment.client.entity.bo.CreateExpBO;
import com.wudimanong.experiment.client.entity.bo.GetExpInfosBO;
import com.wudimanong.experiment.client.entity.bo.UpdateFlowRatioBO;
import com.wudimanong.experiment.client.entity.dto.CreateExpDTO;
import com.wudimanong.experiment.client.entity.dto.GetExpInfosDTO;
import com.wudimanong.experiment.client.entity.dto.UpdateFlowRatioDTO;

/**
 * @author jiangqiao
 */
public interface AbtestExpService {

    /**
     * 分页查询实验信息列表
     *
     * @param getExpInfosDTO
     * @return
     */
    GetExpInfosBO getExpInfos(GetExpInfosDTO getExpInfosDTO);

    /**
     * 创建实验
     *
     * @param createExpDTO
     * @return
     */
    CreateExpBO createExp(CreateExpDTO createExpDTO);

    /**
     * 修改实验流量占比
     *
     * @param updateFlowRatioDTO
     * @return
     */
    UpdateFlowRatioBO updateFlowRatio(UpdateFlowRatioDTO updateFlowRatioDTO);
}
