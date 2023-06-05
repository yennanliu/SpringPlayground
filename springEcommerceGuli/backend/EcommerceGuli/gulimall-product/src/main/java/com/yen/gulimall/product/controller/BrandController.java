package com.yen.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.yen.gulimall.common.valid.AddGroup;
import com.yen.gulimall.common.valid.UpdateGroup;
import com.yen.gulimall.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yen.gulimall.product.entity.BrandEntity;
import com.yen.gulimall.product.service.BrandService;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.R;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){

        BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    //public R save(@Valid @RequestBody BrandEntity brand)
    // ONLY do validation when AddGroup : https://youtu.be/bS08n6JKa-w?t=304
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){

        // https://youtu.be/UT9lRWUwDGQ?t=163
        // replace by general exception handler :
        // com.yen.gulimall.product.exception.GulimallExceptionControllerAdvice

//       Map<String, Object> errors = new HashMap<>();
//        // get all validation error msg
//        result.getFieldErrors().forEach(item -> {
//            // get error msg
//            String msg = item.getDefaultMessage();
//            // get field name
//            String field = item.getField();
//            errors.put(field, msg);
//        });
//        if(result.hasErrors()){
//            return R.error(400, "data validation failed").put("data", errors);
//        }else{
//            brandService.save(brand);
//            return R.ok();
//        }

        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){

        // https://youtu.be/dG2Bo8noDtY?t=1111
        // not only update product table, but update relative redundant columns as well (pms_category_brand_relation)
        //brandService.updateById(brand);
        brandService.updateDetail(brand);
        return R.ok();
    }

    // https://youtu.be/r8naBc3IBNE?t=1023
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){

        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){

        brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

}
