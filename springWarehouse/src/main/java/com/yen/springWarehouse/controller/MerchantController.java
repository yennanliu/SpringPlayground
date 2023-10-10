package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @GetMapping("/toInput")
    public String input(Map<String, Object> map) {

        map.put("Merchant", new Merchant()); // TODO : check necessary ?
        return "merchant/input_merchant";
    }

    @PostMapping("/create")
    public String create(Merchant merchant) {

        merchantService.save(merchant);
        return "redirect:/merchant/list";
    }

    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr) {

        int pageNo;

        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if(pageNo < 1){
            pageNo = 1;
        }

        /*
         * 1st param：which page
         * 2nd param : record count per page
         */
        log.info("pageNo = {}", pageNo);
        Page<Merchant> page = new Page<>(pageNo,3);
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        IPage<Merchant> iPage = merchantService.page(page,
                new LambdaQueryWrapper<Merchant>()
                        .orderByAsc(Merchant::getId)
        );
        log.info("iPage.total = {}, iPage.getPages = {} iPage = {}",  iPage.getTotal(), iPage.getPages(), iPage);
        map.put("page", iPage);

        return "merchant/list_merchant";
    }

    @PostMapping("/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId) {

        merchantService.removeById(typeId);
        return "redirect:/merchant/list";
    }

    @GetMapping(value="/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {

        Merchant merchant = merchantService.getById(typeId);
        log.info(">>> preUpdate typeId : {}, map = {},  merchant = {}", typeId, map, merchant);
        map.put("merchant", merchant);
        return "merchant/update_merchant";
    }

    @PostMapping(value="/update")
    public String update(Merchant merchant) {

        log.info("update merchant type as {}", merchant);
        merchantService.updateById(merchant);
        return "redirect:/merchant/list";
    }

}
