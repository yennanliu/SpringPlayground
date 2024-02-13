package com.yen.springBankApp.controller;

import com.yen.springBankApp.common.ApiResponse;
import com.yen.springBankApp.model.dto.Balance.DeductBalanceDto;
import com.yen.springBankApp.service.BalanceServiceRedisson;
import lombok.extern.slf4j.Slf4j;
import com.yen.springBankApp.model.Balance;
import com.yen.springBankApp.model.dto.Balance.AddBalanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yen.springBankApp.service.BalanceService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceServiceRedisson balanceServiceRedisson;

    @GetMapping("/")
    public ResponseEntity<List<Balance>> getBalanceList(){

        List<Balance> balanceList = balanceService.getBalances();
        return new ResponseEntity<>(balanceList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Balance> getDepartmentByUserId(@PathVariable("userId") Integer userId){

        Balance balance = balanceService.getBalanceByUserId(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

//    @PostMapping("/update")
//    public ResponseEntity<ApiResponse> updateDepartment(@RequestBody DepartmentDto departmentDto) {
//
//        departmentService.updateDepartment(departmentDto);
//        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Department has been updated"), HttpStatus.OK);
//    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBalance(@RequestBody AddBalanceDto addBalanceDto){

        balanceService.addBalance(addBalanceDto);
        return new ResponseEntity<>(new ApiResponse(true, "Balance has been added"), HttpStatus.CREATED);
    }

    @PostMapping("/deduct")
    public ResponseEntity<ApiResponse> deductBalance(@RequestBody DeductBalanceDto deductBalanceDto){

        //balanceService.deductBalance(deductBalanceDto);
        balanceServiceRedisson.deductBalance(deductBalanceDto);
        return new ResponseEntity<>(new ApiResponse(true, "Balance has been deducted !!!"), HttpStatus.CREATED);
    }


}
