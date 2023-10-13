package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.Order;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.util.OrderQueryHelper;
import com.yen.springWarehouse.util.ProductQueryHelper;

public interface OrderService extends IService<Order> {

    Page<Order> getOrderPage(OrderQueryHelper helper, Integer pageNo, Integer pageSize);
}
