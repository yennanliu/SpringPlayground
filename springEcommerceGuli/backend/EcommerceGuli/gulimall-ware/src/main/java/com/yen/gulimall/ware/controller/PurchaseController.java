package com.yen.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yen.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yen.gulimall.ware.entity.PurchaseEntity;
import com.yen.gulimall.ware.service.PurchaseService;
// import com.yen.common.utils.PageUtils;
//import com.yen.common.utils.PageUtils;
//import com.yen.common.utils.R;
import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.common.utils.PageUtils;


/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:24:12
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * Update:
     *  - https://youtu.be/rh5C54pb4RQ?t=194
     */
    @PostMapping("/received")
    public R receive(@RequestBody List<Long> ids){

        purchaseService.receive(ids);
        return R.ok();
    }


    /**
     * Update:
     *  - https://youtu.be/aIDrxpLHylw?t=407
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){

        //PageUtils page = purchaseService.queryPageUnreceive(params);
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * Update:
     *  - https://youtu.be/aIDrxpLHylw?t=142
     */
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){

        PageUtils page = purchaseService.queryPageUnreceive(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){

        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
