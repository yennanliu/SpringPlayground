package com.yen.efence.controller;

// book p. 4-20
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/controller/FenceGeoLayerController.java

import com.yen.efence.dao.mapper.bo.FenceGeoLayerBO;
import com.yen.efence.dao.mapper.bo.dto.FenceGeoLayerSaveDTO;
import com.yen.efence.entity.ResponseResult;
import com.yen.efence.service.FenceGeoLayerService;
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
@RequestMapping("/fence/layer")
public class FenceGeoLayerController {

    @Autowired
    FenceGeoLayerService fenceGeoLayerService;

    /**
     * interface add new geo layer
     *
     * @param fenceGeoLayerSaveDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseResult<FenceGeoLayerBO> save(@RequestBody @Validated FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO){
        return ResponseResult.OK(fenceGeoLayerService.save(fenceGeoLayerSaveDTO));
    }

    /**
     * interface query geo layer
     *
     * @param code
     * @return
     */
    @GetMapping("/getSingle")
    public ResponseResult<FenceGeoLayerBO> getSingle(@RequestParam(value="code") String code){
        return ResponseResult.OK(fenceGeoLayerService.getSingle(code));
    }

}
