package com.yen.springMultiThread.controller;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/controller/CarController.java

import com.yen.springMultiThread.dao.entity.Car;
import com.yen.springMultiThread.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/api/v1/car")
public class CarController {

    @Autowired
    CarService carService;

    //@RequestMapping(method = RequestMethod.POST,  consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity uploadFile(@RequestParam(value = "files") MultipartFile[] files){

        log.info(">>> uploadFile POST request");

        try{
            for (final MultipartFile file : files){ // TODO : check if "final" is necessary
                carService.saveCars(file.getInputStream());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (final Exception e){ // TODO : check if "final" is necessary
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    //@RequestMapping(method = RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody CompletableFuture<ResponseEntity> getAllCars(){

        log.info(">>> getAllCars GET request");

        // TODO : check if can rewrite in non-functional style
        return carService
                .getAllCars()
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }


    // local helper func
    private static Function<Throwable, ResponseEntity<? extends List<Car>>> handleGetCarFailure = throwable -> {
        log.error("Failed to read records: " + throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

}
