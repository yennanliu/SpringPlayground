package service;

import model.Balance;
import model.dto.AddBalanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BalanceRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

}
