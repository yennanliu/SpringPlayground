package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.OptionSchemaRepository;
import com.yen.FlinkRestService.model.OptionSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OptionSchemaService {

    @Autowired
    OptionSchemaRepository optionSchemaRepository;

    public List<OptionSchema> getAllOptions() {

        return optionSchemaRepository.findAll();
    }

    public List<OptionSchema> getAllActiveOptions() {

        List<OptionSchema> activeOptionSchemaList = optionSchemaRepository.findAll().stream().filter(x -> {
            return x.getActive() == true;
        }).collect(Collectors.toList());
        return activeOptionSchemaList;

    }

}
