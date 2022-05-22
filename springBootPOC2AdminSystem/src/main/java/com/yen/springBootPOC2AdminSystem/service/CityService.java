package com.yen.springBootPOC2AdminSystem.service;

import com.yen.springBootPOC2AdminSystem.bean.City;

public interface CityService {

    public City getById(Long id);

    public void saveCity(City city);
}
