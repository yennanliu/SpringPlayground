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

@Slf4j
@Service
public class DepartmentService {

  @Autowired DepartmentRepository departmentRepository;

  public List<Department> getDepartments() {

    // TODO : fix below
    //return departmentRepository.findAll();
    return new ArrayList<>();
  }

  public Department getDepartmentById(Integer departmentId) {

    Department dep = departmentRepository.findById(departmentId).block();
    if (dep != null) {
      //return departmentRepository.findById(departmentId).get();
      return dep;
    }
    log.warn("No department with departmentId = " + departmentId);
    return null;
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

  public void addDepartment(DepartmentDto departmentDto) {

    Department department = new Department();
    BeanUtils.copyProperties(departmentDto, department);
    departmentRepository.save(department);
  }
}
