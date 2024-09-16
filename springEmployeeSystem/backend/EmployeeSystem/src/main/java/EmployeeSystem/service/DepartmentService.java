package EmployeeSystem.service;

import EmployeeSystem.model.Department;
import EmployeeSystem.model.dto.DepartmentDto;
import EmployeeSystem.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DepartmentService {

  @Autowired DepartmentRepository departmentRepository;

  public Flux<Department> getDepartments() {

    // TODO : fix below
    return departmentRepository.findAll();
    //return new ArrayList<>();
  }

  public Mono<Department> getDepartmentById(Integer departmentId) {

    Mono<Department> dep = departmentRepository.findById(departmentId);
//    if (dep != null) {
//      //return departmentRepository.findById(departmentId).get();
//      return dep;
//    }
//    log.warn("No department with departmentId = " + departmentId);
//    return null;
    return dep;
  }

  public void updateDepartment(DepartmentDto departmentDto) {

    // get current department
    Department department = departmentRepository.findById(departmentDto.getId()).block();

    if (department != null){
      // copy attr to new department as updated department
      BeanUtils.copyProperties(departmentDto, department);
    }else{
      log.warn("department is null, id = " + departmentDto.getId());
    }

    // save to DB
    departmentRepository.save(department);
  }

  //@Transactional
  public Mono<Department> addDepartment(DepartmentDto departmentDto) {

    Department department = new Department();
    //BeanUtils.copyProperties(departmentDto, department);
    department.setId(departmentDto.getId());
    department.setName(departmentDto.getName());

//    log.info("(service) add new department start, departmentDto = " + departmentDto);
//    //Mono<Department> departmentMono = departmentRepository.save(department);
//    Flux<Department> departmentMono = departmentRepository.saveAll(Mono.just(department));
//    log.info("(service) add new department end, departmentMono = " + departmentMono);
//    return departmentMono;

    // TODO : fix below
    return departmentRepository.save(department)
            .doOnSuccess(savedDepartment -> {
              // You can log or perform any post-processing here
              log.info("Department saved: " + savedDepartment.getName());
            });

  }
}
