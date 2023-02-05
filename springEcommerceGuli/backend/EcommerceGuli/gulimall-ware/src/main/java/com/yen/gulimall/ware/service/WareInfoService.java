package com.yen.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
//import com.yen.common.utils.PageUtils;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:24:12
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

