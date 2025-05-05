package EmployeeSystem.repository;

import EmployeeSystem.model.Department;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends R2dbcRepository<Department, Integer> {}
