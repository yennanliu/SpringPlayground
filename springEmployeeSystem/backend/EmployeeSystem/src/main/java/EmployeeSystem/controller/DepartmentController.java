package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Department;
import EmployeeSystem.model.dto.DepartmentDto;
import EmployeeSystem.service.DepartmentService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dep")
@Slf4j
public class DepartmentController {

  @Autowired DepartmentService departmentService;

  @GetMapping("/")
  public ResponseEntity<Flux<Department>> getDepartment() {

    Flux<Department> departments = departmentService.getDepartments();
    return new ResponseEntity<>(departments, HttpStatus.OK);
  }

  @GetMapping("/{departmentId}")
  public ResponseEntity<Mono<Department>> getDepartmentById(
      @PathVariable("departmentId") Integer departmentId) {

    Mono<Department> department = departmentService.getDepartmentById(departmentId);
    return new ResponseEntity<>(department, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<ApiResponse> updateDepartment(@RequestBody DepartmentDto departmentDto) {

    departmentService.updateDepartment(departmentDto);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(true, "Department has been updated"), HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<Mono<Department>> addDepartment(@RequestBody DepartmentDto departmentDto) {

    log.info("(controller) add new department, departmentDto = " + departmentDto);
    Mono<Department> departmentMono = departmentService.addDepartment(departmentDto);
//    return new ResponseEntity<>(
//        new ApiResponse(true, "Department has been added"), HttpStatus.CREATED);
    return new ResponseEntity<>(departmentMono, HttpStatus.CREATED);
  }
}
