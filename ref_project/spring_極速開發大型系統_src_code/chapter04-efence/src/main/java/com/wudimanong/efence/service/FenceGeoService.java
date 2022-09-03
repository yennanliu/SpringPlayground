package com.wudimanong.efence.service;

import com.wudimanong.efence.entity.bo.BatchImportGeoFenceBO;
import com.wudimanong.efence.entity.bo.ContainPointBO;
import com.wudimanong.efence.entity.bo.GeoFenceBO;
import com.wudimanong.efence.entity.dto.BatchImportGeoFenceDTO;
import com.wudimanong.efence.entity.dto.ContainPointDTO;
import java.util.List;

/**
 * @author jiangqiao
 */
public interface FenceGeoService {

    /**
     * 批量导入电子围栏业务层方法
     *
     * @param batchImportGeoFenceDTO
     * @return
     */
    List<BatchImportGeoFenceBO> batchImportGeoFence(BatchImportGeoFenceDTO batchImportGeoFenceDTO);

    /**
     * 围栏信息查询
     *
     * @param fenceId
     * @return
     */
    GeoFenceBO getGeofenceById(Integer fenceId);

    /**
     * 围栏信息查询
     *
     * @param containPointDTO
     * @return
     */
    ContainPointBO isContainPoint(ContainPointDTO containPointDTO);
}
