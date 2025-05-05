package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Checkin;
import EmployeeSystem.model.dto.AddCheckinDto;
import EmployeeSystem.service.CheckinService;
import java.util.List;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/checkin")
public class CheckinController {

  @Autowired CheckinService checkinService;

  @GetMapping("/")
  public ResponseEntity<Flux<Checkin>> getCheckin() {

    Flux<Checkin> checkinList = checkinService.getCheckIns();
    return new ResponseEntity<>(checkinList, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Mono<Stream<Checkin>>> getCheckinByUserId(@PathVariable("userId") Integer userId) {

    Mono<Stream<Checkin>> checkinList = checkinService.getCheckinByUserId(userId);
    return new ResponseEntity<>(checkinList, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCheckin(@RequestBody AddCheckinDto addCheckinDto) {

//    if (addCheckinDto.getUserId() < 0){
//      return  new ResponseEntity<>(
//              new ApiResponse(false, "checkin Id < 0"), HttpStatus.BAD_REQUEST);
//    }

    log.info("(CheckinController) add checkIn, addCheckinDto = " + addCheckinDto);
    Mono<Checkin> res = checkinService.addCheckin(addCheckinDto);
    return new ResponseEntity<>(
        new ApiResponse(true, "Checkin has been added"), HttpStatus.CREATED);
  }
}
