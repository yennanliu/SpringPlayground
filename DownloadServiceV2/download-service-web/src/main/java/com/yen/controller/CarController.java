package com.yen.controller;

import com.yen.api.CarApi;
import com.yen.bean.Car;
import com.yen.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController implements CarApi {

    @Autowired
    CarService carService;

    @Override
    public Car[] getAllCar() {
        return carService.getAllCar();
    }

    @Override
    public Car getCarByBrand(@RequestParam("brand") String brand) {
        return carService.getCarByBrand(brand);
    }

    @Override
    public void getAllCar(@RequestBody Car car) {
        carService.addNewCar(car);
    }

}
