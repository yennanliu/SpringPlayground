package com.yen.springMultiThread.service.impl;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/service/CarService.java

import com.yen.springMultiThread.dao.entity.Car;
import com.yen.springMultiThread.dao.repository.CarRepository;
import com.yen.springMultiThread.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository carRepository;

    @Override
    public CompletableFuture<List<Car>> saveCars(InputStream inputStream) throws Exception {

        final long start = System.currentTimeMillis();

        List<Car> cars = parseCSVFile(inputStream);
        log.info("Saving a list of cars of size {} records", cars.size());

        cars = carRepository.saveAll(cars);
        log.info("Elapsed time: {}", (System.currentTimeMillis() - start));

        /**
         *  CompletableFuture
         *      - https://popcornylu.gitbooks.io/java_multithread/content/async/cfuture.html
         *      - https://cloud.tencent.com/developer/article/1845416
         *      - https://www.liaoxuefeng.com/wiki/1252599548343744/1306581182447650
         *      - https://www.readfog.com/a/1633195577082744832
         */
        return CompletableFuture.completedFuture(cars);
    }

    @Async
    @Override
    public CompletableFuture<List<Car>> getAllCars() {

        log.info("Request to get a list of cars");
        final List<Car> cars = carRepository.findAll();
        return CompletableFuture.completedFuture(cars);
    }

    // private helper method
    private List<Car> parseCSVFile(final InputStream inputStream) throws Exception {
        final List<Car> cars = new ArrayList<>();
        try {
            // TODO : optimize this
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line=br.readLine()) != null) {
                    final String[] data = line.split(";");
                    final Car car = new Car();
                    car.setManufacturer(data[0]);
                    car.setModel(data[1]);
                    car.setType(data[2]);
                    cars.add(car);
                }
                return cars;
            }
        } catch(final IOException e) {
            log.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

}
