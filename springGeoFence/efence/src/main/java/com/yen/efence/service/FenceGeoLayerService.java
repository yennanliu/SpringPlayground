package com.yen.efence.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/service/FenceGeoLayerService.java

import com.yen.efence.entity.bo.FenceGeoLayerBO;
import com.yen.efence.entity.dto.FenceGeoLayerSaveDTO;

public interface FenceGeoLayerService {

    /**
     * add new geo layer method
     *
     * @param fenceGeoLayerSaveDTO
     * @return
     */
    FenceGeoLayerBO save(FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO);

    /**
     * get single geo layer method
     *
     * @param code
     * @return
     */
    FenceGeoLayerBO getSingle(String code);
}
