package com.yen.springBootPOC2AdminSystem.service.impl;

// https://www.youtube.com/watch?v=oJGcVUf4rEM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=65

import com.yen.springBootPOC2AdminSystem.bean.City;
import com.yen.springBootPOC2AdminSystem.mapper.CityMapper;
import com.yen.springBootPOC2AdminSystem.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** service for mybatis test */

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    CityMapper cityMapper;

    public City getById(Long id){
        return cityMapper.getById(id);
    }

    public void saveCity(City city){
        cityMapper.insertCity(city);
    }
}
