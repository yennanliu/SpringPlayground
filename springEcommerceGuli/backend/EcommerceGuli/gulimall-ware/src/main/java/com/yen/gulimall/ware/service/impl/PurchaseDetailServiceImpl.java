package com.yen.gulimall.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yen.common.utils.PageUtils;
//import com.yen.common.utils.Query;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.ware.dao.PurchaseDetailDao;
import com.yen.gulimall.ware.entity.PurchaseDetailEntity;
import com.yen.gulimall.ware.service.PurchaseDetailService;


// https://youtu.be/O80QfTFlwSk?t=421
@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();

        // TODO : fix below
//        String key = (String) params.get("key");
//        if (!StringUtils.isEmpty("key")){
//            wrapper.and(w -> {
//                w.eq("purchase_id", key).or()
//                 .eq("sku_id", key);
//            });
//        }

//        String status = (String) params.get("status");
//        if (!StringUtils.isEmpty("status")){
//            queryWrapper.eq("status", status);
//        }
//
//        String wareId = (String) params.get("wareId");
//        if (!StringUtils.isEmpty("wareId")){
//            queryWrapper.eq("ware_id", wareId);
//        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    // https://youtu.be/rh5C54pb4RQ?t=622
    @Override
    public List<PurchaseDetailEntity> listDetailById(Long id) {

        List<PurchaseDetailEntity> purchase_id = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
        return purchase_id;
    }

}