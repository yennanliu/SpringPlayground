package com.wudimanong.efence.service;

import com.wudimanong.efence.entity.bo.FenceGeoLayerBO;
import com.wudimanong.efence.entity.dto.FenceGeoLayerSaveDTO;

/**
 * @author jiangqiao
 */
public interface FenceGeoLayerService {

    /**
     * 图层数据新增业务层方法
     *
     * @param fenceGeoLayerSaveDTO
     * @return
     */
    FenceGeoLayerBO save(FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO);

    /**
     * 获取单个图层信息业务层方法
     *
     * @param code
     * @return
     */
    FenceGeoLayerBO getSingle(String code);
}
