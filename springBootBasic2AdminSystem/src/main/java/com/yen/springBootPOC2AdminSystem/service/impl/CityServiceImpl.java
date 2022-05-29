package com.yen.springBootPOC2AdminSystem.service.impl;

// https://www.youtube.com/watch?v=oJGcVUf4rEM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=65
// https://www.youtube.com/watch?v=p8V2S8LHtRw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=79

import com.yen.springBootPOC2AdminSystem.bean.City;
import com.yen.springBootPOC2AdminSystem.mapper.CityMapper;
import com.yen.springBootPOC2AdminSystem.service.CityService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** service for mybatis test */

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    CityMapper cityMapper;

    Counter counter;

    /** monitor (count) how much CityService is called (actuator)
     *  via io.micrometer.core.instrument.MeterRegistry
     */
    public CityServiceImpl(MeterRegistry meterRegistry){
        counter =  meterRegistry.counter("CityService.saveCity.count");
    }

    public City getById(Long id){
        return cityMapper.getById(id);
    }

    public void saveCity(City city){
        counter.increment();
        cityMapper.insertCity(city);
    }

}
