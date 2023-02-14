package com.yen.springMultiThread.service.impl;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/service/CarService.java

import com.yen.springMultiThread.dao.entity.Car;
import com.yen.springMultiThread.service.CarService;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class CarServiceImpl implements CarService {

    @Override
    public CompletableFuture<List<Car>> saveCars(InputStream inputStream) {
        return null;
    }

    @Override
    public CompletableFuture<List<Car>> getAllCars() {
        return null;
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
