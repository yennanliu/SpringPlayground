package EmployeeSystem.service;

import EmployeeSystem.model.Department;
import EmployeeSystem.model.dto.DepartmentDto;
import EmployeeSystem.repository.DepartmentRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DepartmentService {

  @Autowired DepartmentRepository departmentRepository;

  @Cacheable("departments")
  public List<Department> getDepartments() {

    return departmentRepository.findAll();
  }

  @Cacheable(value = "department", key = "#departmentId")
  public Department getDepartmentById(Integer departmentId) {

    if (departmentRepository.findById(departmentId).isPresent()) {
      return departmentRepository.findById(departmentId).get();
    }
    log.warn("No department with departmentId = " + departmentId);
    return null;
  }

  @Transactional
  @CacheEvict(value = {"departments", "department"}, allEntries = true)
  public void updateDepartment(DepartmentDto departmentDto) {

    // get current department
    Department department = departmentRepository.findById(departmentDto.getId()).get();

    // copy attr to new department as updated department
    BeanUtils.copyProperties(departmentDto, department);

    // save to DB
    departmentRepository.save(department);
  }

  @Transactional
  @CacheEvict(value = {"departments", "department"}, allEntries = true)
  public void addDepartment(DepartmentDto departmentDto) {

    Department department = new Department();
    BeanUtils.copyProperties(departmentDto, department);
    departmentRepository.save(department);
  }
}
