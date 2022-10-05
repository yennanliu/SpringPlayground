//package com.yen.mapper;
//
//// https://hackmd.io/@KaiChen/HJEN-mMVw
//
//import com.yen.bean.Employee;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface EmployeeRepository extends CrudRepository<Employee, Long>{
//
//    @Query("FROM Employee as e")
//    public List<Employee> findByIDPaging(Pageable pageable);
//}