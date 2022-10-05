package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/** book p.140 */

public interface AccountDao extends JpaRepository<Account, Integer> {
}
