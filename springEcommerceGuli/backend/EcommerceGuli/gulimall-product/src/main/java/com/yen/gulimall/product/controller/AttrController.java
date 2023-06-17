package com.yen.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.product.entity.ProductAttrValueEntity;
import com.yen.gulimall.product.service.ProductAttrValueService;
import com.yen.gulimall.product.vo.AttrGroupRelationVo;
import com.yen.gulimall.product.vo.AttrRespVo;
import com.yen.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yen.gulimall.product.entity.AttrEntity;
import com.yen.gulimall.product.service.AttrService;


/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     *  https://youtu.be/MpNzMayYIXY?t=75
     *      /product/attr/info/{attrId}
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable("spuId") Long spuId){

        List<ProductAttrValueEntity> entities =  productAttrValueService.baseAttrListForSpu(spuId);
        return R.ok().put("data", entities);
    }

    /**
     *  New end point
     *      - https://youtu.be/7dVkoxElUvU?t=58
     *  Update
     *      - https://youtu.be/Ga6NMrVkRDY?t=83
     */
    //@GetMapping("/base/list/{catelogId}")
    //@GetMapping("/{attrType}/list/{catelogId}")
    @GetMapping("/base/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId
                          //@PathVariable("attrType") String type
                          ){

        // if type = 1, then base type; if type = 0 then business type
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, "0");
        return R.ok().put("page", page);
    }

    @GetMapping("/sale/list/{catelogId}")
    public R baseAttrList2(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId
                          //@PathVariable("attrType") String type
    ){

        // if type = 1, then base type; if type = 0 then business type
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, "0");
        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *  Update
     *      - https://youtu.be/kCjMunm_9Ig?t=64
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){

		//AttrEntity attr = attrService.getById(attrId);
        AttrRespVo respVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     *  - https://youtu.be/L1hCZ9AumP0?t=626
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){

		//attrService.save(attr);
        attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     *
     * Update:
     *  - https://youtu.be/MpNzMayYIXY?t=304
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> entities){

        productAttrValueService.updateSpuAttr(spuId, entities);
        return R.ok();
    }

    /**
     * 修改
     *
     * Update:
     *  - https://youtu.be/kCjMunm_9Ig?t=454
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){

        //attrService.updateById(attr);
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
