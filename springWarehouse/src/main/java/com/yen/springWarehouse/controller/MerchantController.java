package com.yen.springWarehouse.controller;

import com.github.pagehelper.PageInfo;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.repository.MerchantRepository;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.util.csvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/merchant")
public class MerchantController {

    private final int PAGINATIONSIZE = 3;

    @Autowired
    MerchantService merchantService;

    @Autowired
    MerchantRepository merchantRepository;

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

    @PostMapping("/create_batch")
    public String createBatch(@RequestParam("file") MultipartFile file, Map<String, Object> map) {

        //merchantService.save(merchant);
        if (file.isEmpty()) {
            map.put("message", "Please select a CSV file to upload.");
            map.put("status", false);
            return "redirect:/merchant/list";
        }

        BufferedReader bufferedReader;
        List<String> res = new ArrayList<>();
        csvUtil csv_util = new csvUtil();
        try {
            res = csv_util.loadCsvAsList(file);
            List<Merchant> merchantList = res.stream().map(data -> {
                Merchant merchant = new Merchant();
                String[] _data = data.toString().replace("|", "").split(",");
                merchant.setName(_data[0]);
                merchant.setCity(_data[1]);
                merchant.setType(_data[2]);
                merchant.setStatus(_data[3]);
                return merchant;
            }).collect(Collectors.toList());
            merchantService.saveBatch(merchantList);
            map.put("status", true);
        } catch (Exception e) {
            log.error(">>> load csv failed : " + e);
            map.put("status", false);
        }

        return "redirect:/merchant/list";
    }


    @GetMapping("/list")
    public String list(@RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,
                       @RequestParam(value="pageSize", defaultValue = "0" + PAGINATIONSIZE) int pageSize,
                       Map<String, Object> map) {

        //pageNum = 2;
        log.info(">>> pageNum = {}", pageNum);

//        Page<Merchant> page = new Page<>(pageNum, PAGINATIONSIZE);
//        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
//        IPage<Merchant> iPage = merchantService.page(page,
//                new LambdaQueryWrapper<Merchant>()
//                        .orderByAsc(Merchant::getId)
//        );
//        log.info("iPage.total = {}, iPage.getPages = {} iPage = {}", iPage.getTotal(), iPage.getPages(), iPage);
//        map.put("page", iPage);


        Pageable pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Merchant> merchantsPage = merchantRepository.findAll(pageRequest); //merchantService //postRepository.findAll(pageRequest);
        List<Merchant> merchants = merchantsPage.toList();
        log.info(">>> merchants length = {}", merchants.toArray().length);
        PageInfo<Merchant> pageInfo = null;
        //為了程式的嚴謹性，判斷非空：
        if(pageNum <= 0){
            pageNum = 0;
        }
        log.info("當前頁是："+pageNum+"顯示條數是："+pageSize);
        //1.引入分頁外掛,pageNum是第幾頁，pageSize是每頁顯示多少條,預設查詢總數count
        PageHelper.startPage(pageNum, pageSize);
        //2.緊跟的查詢就是一個分頁查詢-必須緊跟.後面的其他查詢不會被分頁，除非再次呼叫PageHelper.startPage
        try {
            //Page<Post> postList = postRepository.findAll(pageRequest);//service查詢所有的資料的介面
            List<Merchant> merchantList = merchantService.getAllMerchant();//service查詢所有的資料的介面
            log.info(">>> 分頁資料：" + merchantList.get(0).toString());
            //3.使用PageInfo包裝查詢後的結果,5是連續顯示的條數,結果list型別是Page<E>
            pageInfo = new PageInfo<Merchant>(merchantList, pageSize);
            //4.使用model/map/modelandview等帶回前端
            System.out.println(">>> (merchant) pageInfo = " + pageInfo.getPages());

            map.put("pageInfo",pageInfo);
            map.put("merchants", merchants);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 儲存的分頁引數,保證執行緒安全
        }
        return "merchant/list_merchant";
    }

    @PostMapping("/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId) {

        merchantService.removeById(typeId);
        return "redirect:/merchant/list";
    }

    @GetMapping(value = "/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {

        Merchant merchant = merchantService.getById(typeId);
        log.info(">>> preUpdate typeId : {}, map = {},  merchant = {}", typeId, map, merchant);
        map.put("merchant", merchant);
        return "merchant/update_merchant";
    }

    @PostMapping(value = "/update")
    public String update(Merchant merchant) {

        log.info("update merchant type as {}", merchant);
        merchantService.updateById(merchant);
        return "redirect:/merchant/list";
    }

}
