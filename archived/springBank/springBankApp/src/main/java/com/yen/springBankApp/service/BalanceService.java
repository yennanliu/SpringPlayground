package com.yen.springBankApp.service;

import com.yen.springBankApp.model.Balance;
import com.yen.springBankApp.model.dto.Balance.AddBalanceDto;
import com.yen.springBankApp.model.dto.Balance.DeductBalanceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yen.springBankApp.repository.BalanceRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;


    public List<Balance> getBalances() {

        return balanceRepository.findAll();
    }

    public Balance getBalanceByUserId(Integer userId) {

        List<Balance> balanceList = balanceRepository.findAll();
        return balanceList.stream().filter(x -> {return x.getUserId().equals(userId);}
        ).collect(Collectors.toList()).get(0);
    }

    public void addBalance(AddBalanceDto addBalanceDto) {

        Balance balance = new Balance();
        balance.setUserId(addBalanceDto.getUserId());
        balance.setBalance(addBalanceDto.getAmount());
        balance.setCreateTime(new Date());
        balance.setUpdateTime(new Date());
        balanceRepository.save(balance);
    }

    public void deductBalance(DeductBalanceDto deductBalanceDto) {

        Balance balance = this.getBalanceByUserId(deductBalanceDto.getUserId());
        if (balance == null || balance.getUserId() == null){
            throw new RuntimeException("Balance NOT existed : " + deductBalanceDto);
        }
        int currentAmount = balance.getBalance();
        int updatedAmount = currentAmount - deductBalanceDto.getAmount();
        if (updatedAmount >= 0){
            balance.setBalance(updatedAmount);
            balance.setUpdateTime(new Date());
            log.info("new balance : " + updatedAmount);
            balanceRepository.save(balance);
        }
        else{
            log.info("updatedAmount smaller than 0");
        }
    }

}
