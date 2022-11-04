package com.yen.efence.controller;

// book p.4-25, p.4-28
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/controller/FenceGeoController.java

import java.util.List;
import com.yen.efence.entity.ResponseResult;
import com.yen.efence.entity.bo.BatchImportGeoFenceBO;
import com.yen.efence.entity.bo.ContainPointBO;
import com.yen.efence.entity.bo.GeoFenceBO;
import com.yen.efence.entity.dto.BatchImportGeoFenceDTO;
import com.yen.efence.entity.dto.ContainPointDTO;
import com.yen.efence.service.FenceGeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/fence")
public class FenceGeoController {

    @Autowired
    FenceGeoService fenGeoService;

    /** batch import geofence */
    @PostMapping("/batchImportGeoFence")
    public ResponseResult<List<BatchImportGeoFenceBO>> batchImportGeoFence(@RequestBody @Validated BatchImportGeoFenceDTO batchImportGeoFenceDTO){

        return ResponseResult.OK(fenGeoService.batchImportGeoFence(batchImportGeoFenceDTO));
    }

    /** query geofence */
    @GetMapping("/getGeofenceById")
    public ResponseResult<GeoFenceBO> getGeofenceById(@RequestParam(value = "fenceId") Integer fenceId) {
        return ResponseResult.OK(fenGeoService.getGeofenceById(fenceId));
    }

    /** check if in geofence */
    @GetMapping("/isContainPoint")
    public ResponseResult<ContainPointBO> isContainPoint(ContainPointDTO containPointDTO) {
        return ResponseResult.OK(fenGeoService.isContainPoint(containPointDTO));
    }

}
