package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Order;
import com.yen.springWarehouse.mapper.OrderMapper;
import com.yen.springWarehouse.service.OrderService;
import com.yen.springWarehouse.util.OrderQueryHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

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

}
