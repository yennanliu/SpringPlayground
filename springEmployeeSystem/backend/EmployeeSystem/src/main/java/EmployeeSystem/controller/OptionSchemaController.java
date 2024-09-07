package EmployeeSystem.controller;

import EmployeeSystem.model.OptionSchema;
import EmployeeSystem.service.OptionSchemaService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/schema")
public class OptionSchemaController {

  @Autowired OptionSchemaService optionSchemaService;

  @GetMapping("/")
  public ResponseEntity<Flux<OptionSchema>> getSchemaOptions() {

    Flux<OptionSchema> OptionsList = optionSchemaService.getAllOptions();
    return new ResponseEntity<>(OptionsList, HttpStatus.OK);
  }

  @GetMapping("/active/")
  public ResponseEntity<List<OptionSchema>> getActiveSchemaOptions() {

    List<OptionSchema> OptionsList = optionSchemaService.getAllActiveOptions();
    return new ResponseEntity<>(OptionsList, HttpStatus.OK);
  }
}
