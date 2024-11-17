package com.yen.springMultiThread.dao.repository;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/dao/repository/CarRepository.java

import com.yen.springMultiThread.dao.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  JpaRepository
 *      - JpaRepository 是作為 Repository 應用的一種繼承的「抽象介面」，他允許我們可以透過介面的使用，就直接與資料庫進行映射與溝通。
 *      - https://medium.com/learning-from-jhipster/20-controller-service-repository%E7%9A%84%E5%BB%BA%E7%AB%8B-1-jparepository-%E7%9A%84%E4%BD%BF%E7%94%A8-6606de7c9d41
 *      - https://ithelp.ithome.com.tw/articles/10194906
 */

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
