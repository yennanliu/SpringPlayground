package com.yen.mapper;

// https://www.cnblogs.com/xifengxiaoma/p/11027551.html

//import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInfo;
import com.yen.bean.Car;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CarMapper {

    @Select("SELECT * FROM car")
    public Car[] getAllCar();

    @Select("SELECT * FROM car")
    public PageInfo<Car> getAllCarByPage();

    @Select("SELECT * FROM car WHERE brand = #{brand}")
    public Car getCarByBrand(String brand);

    @Insert("INSERT INTO car (`brand`, `price`) VALUES(#{brand},#{price}")
    public void insertCar(Car car);
}
