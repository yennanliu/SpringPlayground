package com.yen.springBootPOC3.service.impl;

import com.yen.springBootPOC3.dao.DemoInfoRepository;
import com.yen.springBootPOC3.entity.DemoInfo;
import com.yen.springBootPOC3.service.DemoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.annotation.Resource;
import java.util.Optional;

/** book p.146 */

public class DemoServiceImpl implements DemoService {

    // attr
    /** we need this '', or will face error. '' means this is an instance */
    public static final String  CACHE_KEY = "'demoInfo'";

    @Resource
    private DemoInfoRepository demoInfoRepository;

    private static final String DEMO_CACHE_NAME = "demo";

    @Override
    public void delete(Long id) {
    }

    @Override
    public DemoInfo update(DemoInfo updated) throws ChangeSetPersister.NotFoundException {
        return null;
    }

    @Override
    public Optional<DemoInfo> findById(long id) {
        return Optional.empty();
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = CACHE_KEY)
    @Override
    public DemoInfo save(DemoInfo demoInfo) {
        return demoInfoRepository.save(demoInfo);
    }

}
