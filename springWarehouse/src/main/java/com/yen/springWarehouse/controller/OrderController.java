package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Order;
import com.yen.springWarehouse.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;


    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr) {

        // TODO : refactor to pageUtil (a common method handle paging request)
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
        Page<Order> page = new Page<>(pageNo,3);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        IPage<Order> iPage = orderService.page(page,
                new LambdaQueryWrapper<Order>()
                        .orderByAsc(Order::getId)
        );
        log.info("iPage.total = {}, iPage.getPages = {} iPage = {}",  iPage.getTotal(), iPage.getPages(), iPage);
        map.put("page", iPage);

        return "order/list_order";
    }

}
