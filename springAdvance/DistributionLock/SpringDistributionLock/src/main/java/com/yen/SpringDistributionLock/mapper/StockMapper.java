package com.yen.SpringDistributionLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StockMapper extends BaseMapper<Stock> {

    @Update("update db_stock set count = count - #{count} where product_code = #{productCode} and count >= #{count}")
    int updateStock(@Param("productCode") String productCode, @Param("count") Integer count);

    // https://youtu.be/tIIOSs4Wd-0?si=My5OYWve-HSD5-cm&t=420
    @Select("select * from db_stock where product_code = #{productCode} for update")
    List<Stock> queryStock(@Param("productCode") String productCode);
}
