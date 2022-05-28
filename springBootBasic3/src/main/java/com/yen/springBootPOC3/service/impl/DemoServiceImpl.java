package com.yen.springBootPOC3.service.impl;

import com.yen.springBootPOC3.dao.DemoInfoRepository;
import com.yen.springBootPOC3.entity.DemoInfo;
import com.yen.springBootPOC3.service.DemoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = DEMO_CACHE_NAME, key = "'demoInfo_' + #id")
    @Override
    public void delete(Long id) {
        System.out.println(">>> simulate delete " + id);
    }

    @CachePut(value = DEMO_CACHE_NAME, key = "'demoInfo_' + #updated.getId()")
    @Override
    public DemoInfo update(DemoInfo updated) throws ChangeSetPersister.NotFoundException {

        Optional<DemoInfo> optionalInfo = demoInfoRepository.findById(updated.getId());

        if (optionalInfo == null){
            // TODO : fix below import (NotFoundException)
            //throw new NotFoundException(">>> Not found");
            throw new RuntimeException(">>> Not found");
        }

        DemoInfo demoInfo;
        optionalInfo.get().setName(updated.getName());
        optionalInfo.get().setPwd(updated.getPwd());
        demoInfo = optionalInfo.get();
        return demoInfo;
    }

    @Cacheable(value = DEMO_CACHE_NAME, key = "'demoInfo_' + '#id'")
    @Override
    public Optional<DemoInfo> findById(long id) {
        System.err.println(">>> not via cache !!! " + id);
        return demoInfoRepository.findById(id);
    }

    @CacheEvict(value = DEMO_CACHE_NAME, key = CACHE_KEY)
    @Override
    public DemoInfo save(DemoInfo demoInfo) {
        return demoInfoRepository.save(demoInfo);
    }

}
