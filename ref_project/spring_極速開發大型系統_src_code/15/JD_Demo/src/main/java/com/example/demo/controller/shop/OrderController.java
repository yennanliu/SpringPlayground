package com.example.demo.controller.shop;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: OrderController
 * Author:   longzhonghua
 * Date:     2019/4/15 10:52
 *
 * @Description: $description$
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/createOrder")
    public String createOrder(Order order) throws Exception {
        Product p = productRepository.findByid(order.getProduct_id());
        order.setStatus(true);
        //價格訊息要從庫中取得,不然黑客偽造訂單,購買虛擬產品就會自動發貨
        //便於示範,這裡取得的是一個產品的價格
        //對於多個產品價格疊加讀者自行撰寫
        order.setAmount(p.getPrice());
        orderRepository.save(order);
        //取得儲存後的資料表自增id
        Long order_id = order.getId();
        //傳遞給支付頁面值
        return "redirect:/pay/?order_id=" + order_id;
    }
}
