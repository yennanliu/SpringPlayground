package com.yen.efence.service.impl;

// book p.4-42
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/service/impl/FenceGeoServiceImpl.java

import com.yen.efence.covert.FenceGeoConvert;
import com.yen.efence.dao.mapper.FenceGeoDao;
import com.yen.efence.dao.model.FenceGeoInfoPO;
import com.yen.efence.entity.bo.BatchImportGeoFenceBO;
import com.yen.efence.entity.bo.ContainPointBO;
import com.yen.efence.entity.bo.GeoFenceBO;
import com.yen.efence.entity.dto.BatchImportGeoFenceDTO;
import com.yen.efence.entity.dto.ContainPointDTO;
import com.yen.efence.service.FenceGeoService;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yen.efence.utils.GeoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.postgis.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class FenceGeoServiceImpl implements FenceGeoService {

    @Autowired
    FenceGeoDao fenceGeoDao;

    /** batch insert geo fence BO layer (transactional) */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BatchImportGeoFenceBO> batchImportGeoFence(BatchImportGeoFenceDTO batchImportGeoFenceDTO) {

        // check, filter on to-insert data
        Map<String, List<GeoFenceBO>> validateFenceData = validateFenceData(batchImportGeoFenceDTO.getFences());
        // get validated data list
        List<GeoFenceBO> successFenceData = validateFenceData.get("success");
        if (successFenceData != null && successFenceData.size() > 0){
            // transform BO data to PO data via MapStruct
            List<FenceGeoInfoPO> successFenceDataPO =  convertSuccessFenceData(batchImportGeoFenceDTO, successFenceData);
            // batch insert
            fenceGeoDao.batchInsert(successFenceDataPO);
        }

        // get in-validated data list
        List<GeoFenceBO> failedFenceData = validateFenceData.get("fail");
        // transform in-validated to output object
        List<BatchImportGeoFenceBO> batchImportGeoFenceBOList = convertFailFenceData(batchImportGeoFenceDTO,
                failedFenceData);

        return batchImportGeoFenceBOList;
    }

    /** get fence info via ID */
    @Override
    public GeoFenceBO getGeofenceById(Integer fenceId) {

        FenceGeoInfoPO fenceGeoInfoPO = fenceGeoDao.selectById(fenceId);
        GeoFenceBO geoFenceBO = null;
        if (fenceGeoInfoPO != null){
            geoFenceBO = FenceGeoConvert.INSTANCE.convertFenceGeoBO(fenceGeoInfoPO);
            geoFenceBO.setRegionGeoJson(fenceGeoInfoPO.getRegion().toString());
        }

        return geoFenceBO;
    }

    /** check whether given geo-data-point is in fence */
    @Override
    public ContainPointBO isContainPoint(ContainPointDTO containPointDTO) {

        String result = fenceGeoDao.isContainPoint(containPointDTO.getLon(), containPointDTO.getLat(), containPointDTO.getFenceId());
        ContainPointBO containPointBO;

        if ("f".equals(result)){
            containPointBO = ContainPointBO.builder().result(new Boolean(false)).build();
        }else{
            containPointBO = ContainPointBO.builder().result(new Boolean(true)).build();
        }

        return containPointBO;
    }

    /** via data check, get validated, and in-validated fence data */
    private Map<String, List<GeoFenceBO>> validateFenceData(List<GeoFenceBO> fenceBOList) {

        //校验结果数据
        Map<String, List<GeoFenceBO>> validateResult = new HashMap<>();
        //合法数据
        List<GeoFenceBO> successGeoFenceBO = new ArrayList<>();
        //非法数据
        List<GeoFenceBO> failGeoFenceBO = new ArrayList<>();
        for (GeoFenceBO geoFenceBO : fenceBOList) {
            //todo 可以根据实际的业务场景对数据进行过滤，这里不做具体实现，直接返回原始输入数据
        }
        validateResult.put("success", fenceBOList);
        return validateResult;
    }

    /** convert validated fence data to PostgreSQL PO (DB data) object */
    private List<FenceGeoInfoPO> convertSuccessFenceData(BatchImportGeoFenceDTO batchImportGeoFenceDTO,
                                                         List<GeoFenceBO> successFenceBOList) {
        List<FenceGeoInfoPO> fenceGeoInfoPOList = new ArrayList<>();
        for (GeoFenceBO geoFenceBO : successFenceBOList) {
            FenceGeoInfoPO fenceGeoInfoPO = FenceGeoConvert.INSTANCE.convertFenceGeoPO(geoFenceBO);
            //将GeoJson转换为Polygon数据类型后进行数据设置
            Polygon regionPolygon = GeoJsonUtil.convertPointArrayJsonToPolygon(geoFenceBO.getRegionGeoJson());
            fenceGeoInfoPO.setRegion(regionPolygon);
            //todo 通过GeoJson计算围栏的中心点（PostGis提供函数ST_Centroid(*)）,也可以通过geoTools工具包提供的方法进行计算
            //todo 通过GeoJson计算围栏面积（PostGis提供函数ST_Area(*)），也可以通过geoTools工具包提供的方法进行计算
            //设置导入批次ID
            fenceGeoInfoPO.setBatchId(batchImportGeoFenceDTO.getBatchId());
            fenceGeoInfoPOList.add(fenceGeoInfoPO);
        }
        return fenceGeoInfoPOList;
    }

    /** convert in-validated fence data (as list) */
    private List<BatchImportGeoFenceBO> convertFailFenceData(BatchImportGeoFenceDTO batchImportGeoFenceDTO,
                                                             List<GeoFenceBO> failFenceBOList) {
        List<BatchImportGeoFenceBO> list = new ArrayList<>();
        //todo 将失败数据转换为接口输出格式，这里仅作示范不作实现
        return list;
    }

}
