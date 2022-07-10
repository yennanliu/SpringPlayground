package com.yen.mapper;

import com.yen.bean.Car;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CarMapper {

    @Select("SELECT * FROM car")
    public Car[] getAllCar();

    @Select("SELECT * FROM car WHERE brand = #{brand}")
    public Car getCarByBrand(String brand);

    @Insert("INSERT INTO car (`brand`, `price`) VALUES(#{brand},#{price}")
    public void insertCar(Car car);
}
