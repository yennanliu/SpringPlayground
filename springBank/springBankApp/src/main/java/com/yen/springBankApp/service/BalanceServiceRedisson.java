package com.yen.springBankApp.service;

import com.yen.springBankApp.model.Balance;
import com.yen.springBankApp.model.dto.Balance.AddBalanceDto;
import com.yen.springBankApp.model.dto.Balance.DeductBalanceDto;
import com.yen.springBankApp.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    private final RReadWriteLock rwLock;

    // constructor
    public BalanceServiceRedisson(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.rwLock = redissonClient.getReadWriteLock("rwLock");
    }

    public List<Balance> getBalances() {

        System.out.println("(BalanceServiceRedisson) getBalances");
        // lock
        this.rwLock.readLock().lock(3, TimeUnit.SECONDS); // default expire time : 3 sec
        try{
            return balanceRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.rwLock.readLock().unlock();
        }
        return null; // return default val ?
    }


    // retry if process can't get lock (?
    // https://medium.com/@htyesilyurt/distributed-lock-with-redisson-rlock-and-spring-boot-redis-pub-sub-86d51fd83e8b
    //@Retryable(value = org.redisson.client.RedisTimeoutException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
    public Balance getBalanceById(Integer id) {

        System.out.println("(BalanceServiceRedisson) getBalanceById start ... Id = " + id);

        Balance balance = new Balance();

        // lock
//        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
//        rwLock.readLock().lock();
        this.rwLock.readLock().lock();
        try{
            if (balanceRepository.findById(id).isPresent()){
                return balanceRepository.findById(id).get();
            }
            throw new RuntimeException("can not get balance with ID = " + id);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            this.rwLock.readLock().unlock();
        }
        return balance;
    }

    public Balance getBalanceByUserId(Integer userId) {

        // lock
        this.rwLock.readLock().lock();
        try{
            List<Balance> balanceList = balanceRepository.findAll();
            return balanceList.stream().filter(x -> {return x.getUserId().equals(userId);}
            ).collect(Collectors.toList()).get(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            this.rwLock.readLock().unlock();
        }
        return null;
    }

    public void addBalance(AddBalanceDto addBalanceDto) {
        // lock
        this.rwLock.readLock().lock();
        try{
            Balance balance = new Balance();
            balance.setUserId(addBalanceDto.getUserId());
            balance.setBalance(addBalanceDto.getAmount());
            balance.setCreateTime(new Date());
            balance.setUpdateTime(new Date());
            balanceRepository.save(balance);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // unlock
            this.rwLock.readLock().unlock();
        }
    }

    public void topUpBalance(){

        log.info(">>> (BalanceServiceRedisson) topUpBalance start ...");
//        // get lock
//        RLock lock = redissonClient.getLock("lock");
//        // lock
//        lock.lock();

        // lock
        this.rwLock.writeLock().lock();

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
            this.rwLock.writeLock().unlock();
        }
    }

    public void deductBalance(DeductBalanceDto deductBalanceDto) {

        log.info(">>> (BalanceServiceRedisson) deductBalance start ...");

//        // get lock
//        //RLock lock = redissonClient.getLock("lock");
//        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
//
//        // lock
//        //lock.lock();
//        rwLock.writeLock();

        // lock
        this.rwLock.writeLock().lock();

        try{
            // sleep 10 sec
            Thread.sleep(10000);

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
            //lock.unlock();
            //rwLock.writeLock().unlock();
            this.rwLock.writeLock().unlock();
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
        //RLock lock = redissonClient.getLock("lock");
        //RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");

        // lock
        //lock.lock();
        this.rwLock.writeLock().lock();

        try{
            // V2 : Mysql
            if (balanceRepository.findById(1).isPresent() && balanceRepository.findById(2).isPresent()){

                Thread.sleep(5000); // 5 sec

//                Balance balance1 = balanceRepository.findById(1).get();
//                Balance balance2 = balanceRepository.findById(2).get();
                // getBalanceById
                Balance balance1 = this.getBalanceById(1);
                Balance balance2 = this.getBalanceById(2);

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
            //lock.unlock();
            this.rwLock.writeLock().unlock();
        }

    }

}
