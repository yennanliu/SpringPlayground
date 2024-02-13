package com.yen.springBankApp.service;

import com.yen.springBankApp.model.Balance;
import com.yen.springBankApp.model.dto.Balance.AddBalanceDto;
import com.yen.springBankApp.model.dto.Balance.DeductBalanceDto;
import com.yen.springBankApp.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BalanceServiceRedisson {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;


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

        log.info(">>> (BalanceServiceRedisson) deductBalance start ...");

        // get lock
        RLock lock = redissonClient.getLock("lock");

        // lock
        lock.lock();

        try{
            // 1) get stock amount
            // set up "balance" as String type in Redis, with value = 5000
            // https://github.com/yennanliu/SpringPlayground/blob/main/springBank/doc/pic/redis_key_setting.png
            String redisBalance = stringRedisTemplate.opsForValue().get("balance").toString();

            if (redisBalance != null && redisBalance.length() != 0){

                Integer balance = Integer.valueOf(redisBalance);
                // 3) update stock to DB
                if (balance > 0) {
                    log.info("deduct OK");
                    stringRedisTemplate.opsForValue().set("balance", String.valueOf(balance - 1));
                }
            }else{
                log.info("balance < 0 !!!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            lock.unlock();
        }
    }

}
