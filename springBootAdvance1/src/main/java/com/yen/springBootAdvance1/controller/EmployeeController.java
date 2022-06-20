package com.yen.springBootAdvance1.controller;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=4
// https://www.youtube.com/watch?v=gfNx_iT6QpE&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=7
// https://www.youtube.com/watch?v=eIZxMWXEPmA&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=8
// https://www.youtube.com/watch?v=oFjcnwkZA3A&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=10

import com.yen.springBootAdvance1.bean.Employee;
import com.yen.springBootAdvance1.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id){
        Employee emp = employeeService.getEmp(id);
        return emp;
    }

    @PostMapping("/emp")
    public Employee update(@RequestParam(name = "id") Integer id,
                           @RequestParam(name = "lastName") String lastName,
                           @RequestParam(name = "email", required=false) String email,
                           @RequestParam(name = "gender", required=false) Integer gender,
                           @RequestParam(name = "dId", required=false) Integer dId){

        Employee employee = new Employee();

        employee.setId(id);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setDId(dId);

        Employee emp = employeeService.updateEmp(employee);
        return emp;
    }

    @PostMapping("/api/foos")
    @ResponseBody
    public String addFoo(@RequestParam(name = "id") String fooId, @RequestParam String name) {
        return "ID: " + fooId + " Name: " + name;
    }

    @GetMapping("/delemp")
    public String delete(Integer id){
        employeeService.deleteEmp(id);
        return "success";
    }

    @GetMapping("/emp/lastname/{lastName}")
    public Employee getEmpByLastName(@PathVariable("lastName") String lastName){
        return employeeService.getEmpByLastName(lastName);
    }

    @PostMapping("/emp/add")
    public void addEmp(@RequestParam(value = "id") int id,
                       @RequestParam(value="lastName") String lastName,
                       @RequestParam(value="email") String email,
                       @RequestParam(value="gender") int gender,
                       @RequestParam(value="dId") int dId){

        Employee employee = new Employee(id, lastName, email, gender, dId);
        System.out.println(">>> insert employee : " + employee);

        employeeService.insertEmp(employee);
    }

}
