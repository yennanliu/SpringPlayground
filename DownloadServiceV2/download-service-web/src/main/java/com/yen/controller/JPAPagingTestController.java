//package com.yen.controller;
//
//// https://hackmd.io/@KaiChen/HJEN-mMVw
//
//import com.yen.bean.Employee;
//
//import com.yen.mapper.EmployeeRepository;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping("/getEmpPaging")
//public class JPAPagingTestController {
//
//    @Autowired
//    EmployeeRepository employeeRepository;
//
//    @GetMapping("/build")
//    public String buildTableAndData(){
//
//        /*** 建立測試資料 ***/
//        for(int i = 0; i < 20;i++) {
//            Employee emp = new Employee();
//            emp.setEmp_name("Tester_"+i);
//            emp.setEmp_team("IT");
//            emp.setEmp_birthDate(new Date(19920101));
//            employeeRepository.save(emp);
//        }
//        return "Done build.";
//    }
//
//    @GetMapping("/{page}/{rows}")
//    public List<Employee> getAllByIDPaging(@PathVariable("page") int page, @PathVariable("rows") int rows){
//
//        Pageable pageable = PageRequest.of(page, rows, Sort.by("id"));
//        return employeeRepository.findByIDPaging(pageable);
//    }
//
//}