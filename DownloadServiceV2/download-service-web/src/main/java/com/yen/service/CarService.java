package com.yen.service;

import com.yen.bean.Car;
import com.yen.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    CarMapper carMapper;

    public Car[] getAllCar(){
        return carMapper.getAllCar();
    }

    public Car getCarByBrand(String brand){
        return carMapper.getCarByBrand(brand);
    }

    public void addNewCar(Car car){
        carMapper.insertCar(car);
    }

}
