package com.yen.controller;

import com.github.pagehelper.Page;
import com.yen.api.CarApi;
import com.yen.bean.Car;
import com.yen.bean.request.CarRequest;
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
    public Page getAllCarPage(@RequestBody CarRequest request) {

        Page cars = carService.getAllCarPage(request.getPageNum(), request.getPageSize());

        System.out.println(">>> getAllCarPage request = " + request);
        System.out.println(">>> getAllCarPage response = " + cars);

        return cars;
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
