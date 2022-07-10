package com.yen.api;

import com.yen.bean.Car;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CarApi {

    @GetMapping("/car/all")
    public Car[] getAllCar();

    @GetMapping("/car")
    public Car getCarByBrand(@RequestParam("brand") String brand);

    @PostMapping("/car/add")
    public void getAllCar(@RequestBody Car car);

}
