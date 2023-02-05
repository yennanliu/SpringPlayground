package com.yen.gulimall.order.dao;

import com.yen.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:04:14
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
