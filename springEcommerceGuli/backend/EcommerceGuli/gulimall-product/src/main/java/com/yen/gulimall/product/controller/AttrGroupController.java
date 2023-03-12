package com.yen.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yen.gulimall.product.entity.AttrEntity;
import com.yen.gulimall.product.service.AttrService;
import com.yen.gulimall.product.service.CategoryService;
import com.yen.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yen.gulimall.product.entity.AttrGroupEntity;
import com.yen.gulimall.product.service.AttrGroupService;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.R;

/**
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */

// https://youtu.be/10yPrgpSEG4?t=26
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    AttrService attrService;

    /**
     * https://youtu.be/7JOhxs7lYbE?t=47
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){

        List<AttrEntity> entities = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", entities);
    }

    /**
     * https://youtu.be/7JOhxs7lYbE?t=316
     */
    public R deleteRelation(AttrGroupRelationVo[] vos){

        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 列表
     *
     * Update: 3 layer product category
     *  - https://youtu.be/10yPrgpSEG4?t=26
     *  - https://youtu.be/L1hCZ9AumP0?t=47
     */
    @RequestMapping("/list/{catalogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catalogId") Long catalogId){

        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catalogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *
     *  Update:
     *      - https://youtu.be/GZk1IbmO1Nc?t=215
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){

        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
