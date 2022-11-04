package com.yen.efence.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/service/FenceGeoService.java

import com.yen.efence.entity.bo.BatchImportGeoFenceBO;
import com.yen.efence.entity.bo.ContainPointBO;
import com.yen.efence.entity.bo.GeoFenceBO;
import com.yen.efence.entity.dto.BatchImportGeoFenceDTO;
import com.yen.efence.entity.dto.ContainPointDTO;

import java.util.List;

public interface FenceGeoService {

    /** batch insert efence method */
    List<BatchImportGeoFenceBO> batchImportGeoFence(BatchImportGeoFenceDTO batchImportGeoFenceDTO);

    /** efence info query */
    GeoFenceBO getGeofenceById(Integer fenceId);

    /** if in efence query */
    ContainPointBO isContainPoint(ContainPointDTO containPointDTO);
}
