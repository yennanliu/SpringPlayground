package com.yen.springBankApp.repository;

import com.yen.springBankApp.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
}
