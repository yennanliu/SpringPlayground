package com.yen.dao;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10

import com.yen.bean.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper // @Repository // better to use @Mapper instead of @Repository (@Repository may cause error when insert op)
public interface PaymentDao {

    @Insert("INSERT INTO payment(serial) VALUES (#{serial});")
    public int create(Payment payment);

    @Select("SELECT * FROM payment WHERE id = #{id};")
    public Payment getPaymentById(Long id);
}
