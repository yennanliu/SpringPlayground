package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.bean.Order;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.mapper.OrderMapper;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.service.OrderService;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.util.OrderQueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    MerchantService merchantService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderMapper orderMapper;


    @Override
    public Page<Order> getOrderPage(OrderQueryHelper helper, Integer pageNo, Integer pageSize) {


        Page<Order> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();

        List<Order> orderList = baseMapper.getOrderList(page, orderWrapper);

        if(CollectionUtils.isNotEmpty(orderList)){
            page.setRecords(orderList);
            return page;
        }

        return new Page<>();
    }

    @Override
    public Boolean updateProductWithOrder(Order order) {

        if (order.getMerchantId() == null || order.getProductId() == null){
            log.error("merchantId or ProductId is null");
            return false;
        }

        Product product = productService.getById(order.getProductId());
        Merchant merchant = merchantService.getById(order.getMerchantId());

        // TODO : optimize below
        // TODO : send error msg to FE
        // merchant doesn't own product
        if (product.getMerchantId() != merchant.getId()){
            log.error("merchant doesn't own product");
            return false;
        }

        int diff = product.getAmount() - order.getAmount();
        if (diff < 0){
            log.error("product amount less than order amount");
            return false;
        }

        // update product
        product.setAmount(diff);
        productService.updateById(product);
        return true;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

}
