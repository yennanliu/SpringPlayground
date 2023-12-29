package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Checkin;
import EmployeeSystem.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    @Autowired
    CheckinService checkinService;

    @GetMapping("/")
    public ResponseEntity<List<Checkin>> getCheckin(){

        List<Checkin> checkinList = checkinService.getCheckIns();
        return new ResponseEntity<>(checkinList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Checkin>> getCheckinByUserId(@PathVariable("userId") Integer userId){

        List<Checkin> checkinList = checkinService.getCheckinByUserId(userId);
        return new ResponseEntity<>(checkinList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCheckin(@RequestBody Checkin checkin){

        checkinService.addCheckin(checkin.getUserID());
        return new ResponseEntity<>(new ApiResponse(true, "Checkin has been added"), HttpStatus.CREATED);
    }

}
