package com.yen.springBootPOC2AdminSystem.mapper;

// https://www.youtube.com/watch?v=oJGcVUf4rEM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=65

import com.yen.springBootPOC2AdminSystem.bean.City;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/** mapper for mybatis map City */

@Mapper
public interface CityMapper {

    @Select("SELECT * FROM city WHERE id = #{id}")
    public City getById(Long id);

    @Insert("INSERT INTO city(`name`, `state`, `country`) values(#{name}, #{state}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insertCity(City city);
}
