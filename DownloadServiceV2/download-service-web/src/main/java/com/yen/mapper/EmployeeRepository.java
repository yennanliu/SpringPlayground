package com.yen.mapper;

import com.yen.bean.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.CrudRepository;

@Mapper
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
