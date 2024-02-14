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

    public void topUpBalance(){

        log.info(">>> (BalanceServiceRedisson) topUpBalance start ...");

        // get lock
        RLock lock = redissonClient.getLock("lock");

        // lock
        lock.lock();

        try{
            // V2 : Mysql
            if (balanceRepository.findById(1).isPresent()){
                Balance balance1 = balanceRepository.findById(1).get();
                // update to DB
                if (balance1.getBalance() > 0){
                    balance1.setBalance(balance1.getBalance() + 1);
                    balanceRepository.save(balance1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            lock.unlock();
        }
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
                    stringRedisTemplate.opsForValue().set("balance", String.valueOf(balance - 1));
                    log.info("deduct OK");
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

    public void transferRedis(DeductBalanceDto deductBalanceDto) {

        log.info(">>> (BalanceServiceRedisson) transferRedis start ...");

        // get lock
        RLock lock = redissonClient.getLock("lock");

        // lock
        lock.lock();

        try{
            // 1) get stock amount
            // set up "balance-1" as String type in Redis, with value = 5000
            // set up "balance-2" as String type in Redis, with value = 5000
            // https://github.com/yennanliu/SpringPlayground/blob/main/springBank/doc/pic/redis_key_setting.png
            String redisBalance1 = stringRedisTemplate.opsForValue().get("balance-1").toString();
            String redisBalance2 = stringRedisTemplate.opsForValue().get("balance-2").toString();

            if (redisBalance1 != null && redisBalance1.length() != 0 && redisBalance2 != null && redisBalance2.length() != 0){

                Integer balance1 = Integer.valueOf(redisBalance1);
                Integer balance2 = Integer.valueOf(redisBalance2);
                // 3) update stock to DB
                if (balance1 > 0 && balance2 > 0) {
                    stringRedisTemplate.opsForValue().set("balance-1", String.valueOf(balance1 - 1));
                    stringRedisTemplate.opsForValue().set("balance-2", String.valueOf(balance2 + 1));
                    log.info("transfer OK");
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

    public void transferMysql(DeductBalanceDto deductBalanceDto) {

        log.info(">>> (BalanceServiceRedisson) transferMysql start ...");

        // get lock
        RLock lock = redissonClient.getLock("lock");

        // lock
        lock.lock();

        try{
            // V2 : Mysql
            if (balanceRepository.findById(1).isPresent() && balanceRepository.findById(2).isPresent()){
                Balance balance1 = balanceRepository.findById(1).get();
                Balance balance2 = balanceRepository.findById(2).get();
                // update to DB
                if (balance1.getBalance() > 0 && balance2.getBalance() > 0){
                    balance1.setBalance(balance1.getBalance() - 1);
                    balance2.setBalance(balance2.getBalance() + 1);
                    balanceRepository.save(balance1);
                    balanceRepository.save(balance2);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            lock.unlock();
        }

    }

}
