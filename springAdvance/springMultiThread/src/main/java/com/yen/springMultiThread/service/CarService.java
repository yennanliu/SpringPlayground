package com.yen.springMultiThread.service;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/service/CarService.java

import com.yen.springMultiThread.dao.entity.Car;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface CarService {

    @Async
    public CompletableFuture<List<Car>> saveCars(final InputStream inputStream) throws Exception;

    @Async
    public CompletableFuture<List<Car>> getAllCars();

}
