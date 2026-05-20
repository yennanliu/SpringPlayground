package EmployeeSystem.service;

import EmployeeSystem.model.OptionSchema;
import EmployeeSystem.repository.OptionSchemaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OptionSchemaService {

  @Autowired OptionSchemaRepository optionSchemaRepository;

  @Cacheable(value = "option-schemas", key = "'all'")
  public List<OptionSchema> getAllOptions() {

    return optionSchemaRepository.findAll();
  }

  @Cacheable(value = "option-schemas", key = "'active'")
  public List<OptionSchema> getAllActiveOptions() {
    return optionSchemaRepository.findByActiveTrue();
  }
}
