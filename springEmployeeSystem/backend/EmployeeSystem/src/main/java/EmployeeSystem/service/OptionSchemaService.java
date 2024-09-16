package EmployeeSystem.service;

import EmployeeSystem.model.OptionSchema;
import EmployeeSystem.repository.OptionSchemaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class OptionSchemaService {

  @Autowired OptionSchemaRepository optionSchemaRepository;

  public Flux<OptionSchema> getAllOptions() {

    return optionSchemaRepository.findAll();
  }

  public List<OptionSchema> getAllActiveOptions() {

    List<OptionSchema> activeOptionSchemaList =
        optionSchemaRepository.findAll().toStream()
            .filter(
                x -> {
                  return x.getActive() == true;
                })
            .collect(Collectors.toList());
    return activeOptionSchemaList;
  }
}
