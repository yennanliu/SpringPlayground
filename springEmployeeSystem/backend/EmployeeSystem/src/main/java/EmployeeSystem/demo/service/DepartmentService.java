package EmployeeSystem.demo.service;

import EmployeeSystem.demo.model.Department;
import EmployeeSystem.demo.repository.DepartmentRepository;
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

}
