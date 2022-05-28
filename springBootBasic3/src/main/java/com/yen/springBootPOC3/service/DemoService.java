package com.yen.springBootPOC3.service;

import com.yen.springBootPOC3.entity.DemoInfo;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** book p.146 */

@Service
public interface DemoService{

    // abstract method
    void delete(Long id);

    DemoInfo update(DemoInfo updated) throws ChangeSetPersister.NotFoundException;

    Optional<DemoInfo> findById(long id);

    DemoInfo save(DemoInfo demoInfo);
}
