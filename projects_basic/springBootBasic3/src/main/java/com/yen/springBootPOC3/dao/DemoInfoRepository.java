package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.DemoInfo;
import org.springframework.data.repository.CrudRepository;

/** book p.145 */

public interface DemoInfoRepository extends CrudRepository<DemoInfo, Long> {
}
