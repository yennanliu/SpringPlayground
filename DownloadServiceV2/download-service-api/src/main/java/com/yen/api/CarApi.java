package com.yen.api;

import com.github.pagehelper.Page;
import com.yen.bean.Car;
import com.yen.bean.request.CarRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CarApi {

    @GetMapping("/car/all")
    public Car[] getAllCar();

    @PostMapping("/car/all/page")
    public Page getAllCarPage(@RequestBody CarRequest carRequest);

    @GetMapping("/car")
    public Car getCarByBrand(@RequestParam("brand") String brand);

    @PostMapping("/car/add")
    public void getAllCar(@RequestBody Car car);

}
