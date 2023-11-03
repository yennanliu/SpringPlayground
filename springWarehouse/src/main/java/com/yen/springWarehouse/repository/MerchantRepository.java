package com.yen.springWarehouse.repository;

import com.yen.springWarehouse.bean.Merchant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public interface MerchantRepository extends PagingAndSortingRepository<Merchant, Long> {
}
