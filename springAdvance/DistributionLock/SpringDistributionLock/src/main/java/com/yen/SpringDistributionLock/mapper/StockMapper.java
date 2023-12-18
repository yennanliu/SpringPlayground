package com.yen.SpringDistributionLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StockMapper extends BaseMapper<Stock> {

    @Update("update db_stock set count = count - #{count} where product_code = #{productCode} and count >= #{count}")
    int updateStock(@Param("productCode") String productCode, @Param("count") Integer count);
}
