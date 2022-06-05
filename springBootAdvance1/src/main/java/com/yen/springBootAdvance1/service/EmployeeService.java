package com.yen.springBootAdvance1.service;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=4

import com.yen.springBootAdvance1.bean.Employee;
import com.yen.springBootAdvance1.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    public Employee getEmp(Integer id){
        System.out.println(">>> query employee with id = " + id);
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }


}
