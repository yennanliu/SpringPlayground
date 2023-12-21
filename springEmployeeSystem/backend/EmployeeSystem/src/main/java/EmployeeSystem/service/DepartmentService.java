package EmployeeSystem.service;

import EmployeeSystem.model.Department;
import EmployeeSystem.model.dto.DepartmentDto;
import EmployeeSystem.repository.DepartmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Department> getDepartments() {

        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer departmentId) {

        return departmentRepository.findById(departmentId).get();
    }

    public void updateDepartment(DepartmentDto departmentDto) {

        // get current department
        Department department = departmentRepository.findById(departmentDto.getId()).get();

        // copy user to updated department
        BeanUtils.copyProperties(departmentDto, department);

        // save to DB
        departmentRepository.save(department);
    }

    public void addDepartment(DepartmentDto departmentDto) {

        Department department = new Department();
        BeanUtils.copyProperties(departmentDto, department);
        departmentRepository.save(department);
    }

}
