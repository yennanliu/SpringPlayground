package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.model.SqlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SqlJobService {

    // TODO : read from conf
    private String BASE_URL = "http://localhost:8081/";

    @Autowired
    private SqlJobRepository sqlJobRepository;
}
