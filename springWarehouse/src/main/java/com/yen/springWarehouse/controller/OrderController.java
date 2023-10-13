package com.yen.springWarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Order;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.service.OrderService;
import com.yen.springWarehouse.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    MerchantService merchantService;

    @Autowired
    ProductService productService;

    @GetMapping("/toInput")
    public String input(Map<String, Object> map) {

        map.put("merchantList", merchantService.list());
        map.put("productList", productService.list());
        map.put("Order", new Order()); // TODO : check necessary ?
        return "order/input_order";
    }

    @PostMapping("/create")
    public String create(Order order) {

        log.info("(OrderController.create) Create new order : " + order.toString());
        orderService.save(order);
        return "redirect:/order/list";
    }

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

    @GetMapping(value="/preUpdate/{orderNo}")
    public String preUpdate(@PathVariable("orderNo") String orderNo, Map<String, Object> map) {

        Order order = orderService.getById(orderNo);
        map.put("order" , order);
        map.put("orderList", orderService.list());
        return "product/update_order";
    }

    @PostMapping(value="/update")
    public String update(Order order) {

        log.info("update order as {}", order);
        orderService.updateById(order);
        return "redirect:/order/list";
    }

}
