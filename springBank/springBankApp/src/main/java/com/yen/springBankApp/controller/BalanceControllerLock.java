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
@RequestMapping("/balance_lock")
public class BalanceControllerLock {

    @Autowired
    private BalanceServiceRedisson balanceServiceRedisson;

    @GetMapping("/")
    public ResponseEntity<List<Balance>> getBalanceList(){

        List<Balance> balanceList = balanceServiceRedisson.getBalances();
        return new ResponseEntity<>(balanceList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Balance> getDepartmentByUserIdWithLock(@PathVariable("userId") Integer userId){

        Balance balance = balanceServiceRedisson.getBalanceById(userId);
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

        balanceServiceRedisson.addBalance(addBalanceDto);
        return new ResponseEntity<>(new ApiResponse(true, "Balance has been added"), HttpStatus.CREATED);
    }

    @PostMapping("/deduct")
    public ResponseEntity<ApiResponse> deductBalance(@RequestBody DeductBalanceDto deductBalanceDto){

        balanceServiceRedisson.deductBalance(deductBalanceDto);
        return new ResponseEntity<>(new ApiResponse(true, "Balance has been deducted !!!"), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transfer(@RequestBody DeductBalanceDto deductBalanceDto){

        //balanceServiceRedisson.transferRedis(deductBalanceDto);
        balanceServiceRedisson.transferMysql(deductBalanceDto);
        return new ResponseEntity<>(new ApiResponse(true, "Balance has been transferred !!!"), HttpStatus.CREATED);
    }

}
